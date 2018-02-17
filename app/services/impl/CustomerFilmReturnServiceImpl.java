package services.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import jooq.jooqobjects.tables.pojos.Customer;
import jooq.jooqobjects.tables.pojos.CustomerFilmRent;
import model.NewCustomerFilmReturn;
import model.NewCustomerFilmReturnRequest;
import model.pricecalculator.FilmPriceCalculator;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import repositories.CustomerRentRepository;
import repositories.CustomerRepository;
import repositories.FilmRepository;
import services.CustomerFilmReturnService;
import utils.FilmPriceCalculatorUtils;
import utils.Validation;
import utils.Validator;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CustomerFilmReturnServiceImpl implements CustomerFilmReturnService {

    private CustomerRentRepository customerRentRepository;
    private CustomerRepository customerRepository;
    private FilmRepository filmRepository;
    private FilmPriceCalculator filmPriceCalculator;


    @Inject
    public CustomerFilmReturnServiceImpl(CustomerRentRepository customerRentRepository,
                                         FilmRepository filmRepository,
                                         CustomerRepository customerRepository) {
        this.customerRentRepository = customerRentRepository;
        this.filmRepository = filmRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public CompletionStage<Void> returnFilms(DSLContext create, NewCustomerFilmReturnRequest filmReturnRequest, LocalDate returnDate) {
        return CompletableFuture.runAsync(() -> {
            // check that the films to return and the customer are OK
            checkFilmsAndCustomerAvailable(create, filmReturnRequest);

            // prepare the price calculator to compute any possible late return charges
            filmPriceCalculator = FilmPriceCalculatorUtils.prepareFilmPriceCalculator();

            // retrieve the rented films pending to be returned by this customer from the persistent storage
            List<CustomerFilmRent> rentedFilms = customerRentRepository.findRentedFilmsByIdAndCustomerCode(
                    create,
                    filmReturnRequest.getFilmIds(),
                    filmReturnRequest.getCustomerCode());

            // create the NewCustomerFilmReturn objects holding the details of the return
            List<NewCustomerFilmReturn> customerFilmReturns = new ArrayList<>();

            // keep track of possible late charges
            BigDecimal totalLateReturnPrice = BigDecimal.valueOf(0);

            // for each film generate a NewCustomerFilmReturn object with the details of the return
            for (Integer filmId : filmReturnRequest.getFilmIds()) {
                BigDecimal filmPrice = filmRepository.findFilmPricebyId(create, filmId);
                CustomerFilmRent rentedFilm = findFilmRent(rentedFilms, filmId);

                NewCustomerFilmReturn customerFilmReturn = new NewCustomerFilmReturn(
                        filmId,
                        rentedFilm.getRentDate(),
                        rentedFilm.getNumberRentDays(),
                        returnDate,
                        filmPrice);

                BigDecimal lateReturnCharge = filmPriceCalculator.calculateLateReturnCharge(customerFilmReturn);
                customerFilmReturn.setLateReturnCharge(lateReturnCharge);
                totalLateReturnPrice = totalLateReturnPrice.add(lateReturnCharge);
                customerFilmReturns.add(customerFilmReturn);
            }

            // mark the films as returned by setting the return date and storing any possible late charges
            customerRentRepository.markFilmsAsReturned(create, filmReturnRequest.getCustomerCode(), customerFilmReturns);
            // update the customer balance in case of late return charges
            customerRepository.updateCustomerBalance(create, filmReturnRequest.getCustomerCode(), totalLateReturnPrice);
        });
    }


    /**
     * Finds the {@link CustomerFilmRent} with the given film id
     *
     * @param customerRents the films rented by the customer
     * @param filmId        the id of the film with which to look for the rented film details
     * @return the CustomerFilmRent object corresponding to the film id
     */
    private CustomerFilmRent findFilmRent(List<CustomerFilmRent> customerRents, Integer filmId) {
        return customerRents
                .stream()
                .filter(entry -> entry.getFilmId().equals(filmId))
                .findFirst()
                .get();
    }


    /**
     * Checks that the films to return and the customer code are valid
     *
     * @param create            the {@link DSLContext} with which to access and query the database using
     *                          the typesafe SQL Java DSL provided by JOOQ
     * @param filmReturnRequest the film return request to validate
     */
    private void checkFilmsAndCustomerAvailable(DSLContext create,
                                                NewCustomerFilmReturnRequest filmReturnRequest) {
        Validator validator = Validator.apply(
                Validation.with(
                        filmReturnRequest.getFilmIds() != null && !filmReturnRequest.getFilmIds().isEmpty(),
                        "There must be films to return"),
                Validation.with(
                        StringUtils.isNotEmpty(filmReturnRequest.getCustomerCode()),
                        "The customer code must not be empty")
        );

        if (StringUtils.isNotEmpty(filmReturnRequest.getCustomerCode())) {
            final Optional<Customer> maybeCustomer = customerRepository.findCustomerByCustomerCode(create, filmReturnRequest.getCustomerCode());
            validator.add(Validation.with(
                    maybeCustomer.isPresent(),
                    "The customer code does not exist"));
        }

        if (filmReturnRequest.getFilmIds() != null && !filmReturnRequest.getFilmIds().isEmpty()) {
            // retrieve the identifiers of the films that do not exist
            final List<Integer> notExistingFilmIds = filmRepository.findNotExistingFilmIds(create, filmReturnRequest.getFilmIds());
            // retrieve the identifiers of the rented films
            final List<Integer> rentedFilmIds = customerRentRepository.findRentedFilmsIdsByIdAndCustomerCode(
                    create,
                    filmReturnRequest.getFilmIds(),
                    filmReturnRequest.getCustomerCode());

            // get the identifiers of the films not rented by this customer from the ones passed in the NewCustomerFilmReturnRequest
            List<Integer> filmIdsNotRentedByCustomer = new ArrayList<>(
                    Collections2.filter(filmReturnRequest.getFilmIds(), Predicates.not(Predicates.in(rentedFilmIds))));

            validator.add(
                    Validation.with(
                            filmIdsNotRentedByCustomer.isEmpty(),
                            "Cannot return films not rented by this customer. The list of erroneous film identifiers is: "
                                    + Joiner.on(", ").join(filmIdsNotRentedByCustomer)),
                    Validation.with(
                            notExistingFilmIds.isEmpty(),
                            "There are films that cannot be returned because they do not exist. The list of " +
                                    "identifiers is: " + Joiner.on(", ").join(notExistingFilmIds))
            );
        }

        validator.validate();
    }

}
