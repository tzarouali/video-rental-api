package services.impl;

import com.google.common.base.Joiner;
import exceptions.ZeroPriceComputationException;
import jooq.jooqobjects.tables.pojos.Customer;
import model.*;
import model.pricecalculator.FilmPriceCalculator;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import repositories.CustomerRentRepository;
import repositories.CustomerRepository;
import repositories.FilmRepository;
import services.CustomerFilmRentService;
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

public class CustomerFilmRentServiceImpl implements CustomerFilmRentService {

    private CustomerRentRepository customerRentRepository;
    private CustomerRepository customerRepository;
    private FilmRepository filmRepository;
    private FilmPriceCalculator filmPriceCalculator;


    @Inject
    public CustomerFilmRentServiceImpl(CustomerRentRepository customerRentRepository,
                                       CustomerRepository customerRepository,
                                       FilmRepository filmRepository) {
        this.customerRentRepository = customerRentRepository;
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
    }


    @Override
    public CompletionStage<List<CustomerFilmRentSummary>> findAll(DSLContext create) {
        return CompletableFuture.supplyAsync(() -> customerRentRepository.findAll(create));
    }


    @Override
    public CompletionStage<Void> rentFilms(DSLContext create, NewCustomerFilmRentRequest customerRents, LocalDate rentDate) {
        return CompletableFuture.runAsync(() -> {
            filmPriceCalculator = FilmPriceCalculatorUtils.prepareFilmPriceCalculator();

            // check that the film rental details are OK
            checkFilmsAndRentDaysAndCustomerAvailable(create, customerRents);

            final FilmsRentalPriceDetails filmRentalPrice = computeFilmRentalPriceAndTotalBonusPoints(create, customerRents);

            // check that we have available funds
            checkCustomerCodeAndFunds(create, customerRents.getCustomerCode(), filmRentalPrice.getTotalRentalPrice());

            // once the checks are done, we can create the list of NewCustomerFilmRent objects to pass to the
            // repository in order to store the actual film rental
            List<NewCustomerFilmRent> newCustomerFilmRents = new ArrayList<>();
            customerRents.getFilmIds().forEach(filmId -> {
                // get the object containing the film rental price and the bonus points gained in renting it
                FilmIdWithRentPriceAndBonus filmIdWithRentPriceAndBonus = findFilmIdWithRentPriceAndBonus(
                        filmRentalPrice.getFilmsWithPrices(),
                        filmId);

                newCustomerFilmRents.add(new NewCustomerFilmRent(
                        customerRents.getCustomerCode(),
                        filmId,
                        filmIdWithRentPriceAndBonus.getComputedRentPrice(),
                        filmIdWithRentPriceAndBonus.getComputedBonusPoints(),
                        customerRents.getNumberRentDays(),
                        rentDate)
                );
            });

            // store the films rented
            customerRentRepository.saveCustomerRent(create, newCustomerFilmRents);
            // and update the customer's balance & bonus points
            customerRepository.updateCustomerBonusPointsAndBalance(
                    create,
                    customerRents.getCustomerCode(),
                    filmRentalPrice.getTotalRentalPrice(),
                    filmRentalPrice.getTotalRentalBonusPoints());
        });
    }


    /**
     * Finds the {@link FilmIdWithRentPriceAndBonus} object for a given film id from the ones given in the list
     *
     * @param filmIdWithRentPriceAndBonuses the list of films with their prices and bonuses
     * @param filmId                        the id of the film we're interested in getting
     * @return the matching {@link FilmIdWithRentPriceAndBonus}
     */
    private FilmIdWithRentPriceAndBonus findFilmIdWithRentPriceAndBonus(List<FilmIdWithRentPriceAndBonus> filmIdWithRentPriceAndBonuses,
                                                                        Integer filmId) {
        return filmIdWithRentPriceAndBonuses
                .stream()
                .filter(entry -> entry.getFilmId().equals(filmId))
                .findFirst()
                .get();
    }


    /**
     * Computes the total rental price, the price per film and the total bonus points per film
     *
     * @param create        the {@link DSLContext} with which to access and query the database using
     *                      the typesafe SQL Java DSL provided by JOOQ
     * @param customerRents the film rental request made by the client
     * @return a {@link FilmsRentalPriceDetails} object with the computed values
     */
    private FilmsRentalPriceDetails computeFilmRentalPriceAndTotalBonusPoints(DSLContext create,
                                                                              NewCustomerFilmRentRequest customerRents) {
        BigDecimal totalRentPrice = BigDecimal.ZERO;
        int totalBonusPoints = 0;
        List<FilmIdWithRentPriceAndBonus> filmIdWithPrices = new ArrayList<>();

        // for each film the customer wants to rent, increase the total amount and compute the film rental price
        for (Integer filmId : customerRents.getFilmIds()) {
            // the film details will contain among other fields, the price for this type of film and the number of
            // days this price is the same
            FilmDetails filmDetails = filmRepository.findFilmDetailsById(create, filmId);

            // compute the film rent price and bonus points
            BigDecimal filmRentPrice = filmPriceCalculator.calculateRentPrice(filmDetails, customerRents.getNumberRentDays());
            int filmRentBonusPoints = filmDetails.getDefaultBonusPoints();

            // add the film bonus points and price to the totals
            totalRentPrice = totalRentPrice.add(filmRentPrice);
            totalBonusPoints += filmRentBonusPoints;

            filmIdWithPrices.add(new FilmIdWithRentPriceAndBonus(filmId, filmRentPrice, filmRentBonusPoints));
        }

        // Error out in case the computed total price is <= 0
        if (totalRentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ZeroPriceComputationException("Error computing the total rent price. The price cannot be zero!");
        }

        return new FilmsRentalPriceDetails(totalRentPrice, filmIdWithPrices, totalBonusPoints);
    }


    /**
     * Checks that the films, rent days and the customer code are available inside the request
     *
     * @param create        the {@link DSLContext} with which to access and query the database using
     *                      the typesafe SQL Java DSL provided by JOOQ
     * @param customerRents the film rental request made by the client
     */
    private void checkFilmsAndRentDaysAndCustomerAvailable(DSLContext create,
                                                           NewCustomerFilmRentRequest customerRents) {
        Validator validator = Validator.apply(
                Validation.with(
                        customerRents.getFilmIds() != null && !customerRents.getFilmIds().isEmpty(),
                        "There must be films to rent"),
                Validation.with(
                        StringUtils.isNotEmpty(customerRents.getCustomerCode()),
                        "The customer code must not be empty"),
                Validation.with(
                        customerRents.getNumberRentDays() != null && customerRents.getNumberRentDays() > 0,
                        "The number of rent days must not be empty and must be greater than 0")
        );

        if (customerRents.getFilmIds() != null && !customerRents.getFilmIds().isEmpty()) {
            // retrieve the identifiers of the films that do not exist
            final List<Integer> notExistingFilmIds = filmRepository.findNotExistingFilmIds(create, customerRents.getFilmIds());
            // retrieve the identifers of the films already rented
            final List<Integer> rentedFilmIds = customerRentRepository.findRentedFilmsIdsById(create, customerRents.getFilmIds());
            validator.add(
                    Validation.with(
                            rentedFilmIds.isEmpty(),
                            "There are films that cannot be rented because they are not available. The list of "
                                    + "identifiers is: " + Joiner.on(", ").join(rentedFilmIds)),
                    Validation.with(
                            notExistingFilmIds.isEmpty(),
                            "There are films that cannot be rented because they do not exist. The list of " +
                                    "identifiers is: " + Joiner.on(", ").join(notExistingFilmIds))
            );
        }

        validator.validate();
    }


    /**
     * Checks that the customer exists and that there are enough funds for the rental
     *
     * @param create         the {@link DSLContext} with which to access and query the database using
     *                       the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode   the customer code
     * @param totalRentPrice the total price to pay for the rental
     */
    private void checkCustomerCodeAndFunds(DSLContext create,
                                           String customerCode,
                                           BigDecimal totalRentPrice) {
        final Optional<Customer> maybeCustomer = customerRepository.findCustomerByCustomerCode(create, customerCode);
        final Optional<BigDecimal> maybeFunds = customerRepository.findAvailableMoneyByCustomerCode(create, customerCode);
        Validator.apply(
                Validation.with(
                        maybeCustomer.isPresent(),
                        "The customer code does not exist"),
                Validation.with(
                        maybeFunds.isPresent() && enoughFundsForRenting(maybeFunds.get(), totalRentPrice),
                        "No funds found or insufficient funds to rent the films for customer "
                                + customerCode)
        ).validate();
    }


    /**
     * Checks that there are enough funds to rent the films
     *
     * @param availableMoney the customer's available money
     * @param totalRentPrice the total price to pay for the rental
     * @return True if the customer has enough funds to rent the films or False otherwise
     */
    private boolean enoughFundsForRenting(BigDecimal availableMoney, BigDecimal totalRentPrice) {
        return availableMoney.compareTo(totalRentPrice) >= 0;
    }

}
