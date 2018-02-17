package services;

import model.CustomerFilmRentSummary;
import model.NewCustomerFilmRentRequest;
import org.jooq.DSLContext;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;

public interface CustomerFilmRentService {

    /**
     * Retrieves all the available {@link CustomerFilmRentSummary} objects with the details of the films rented
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of {@link CustomerFilmRentSummary} objects
     */
    CompletionStage<List<CustomerFilmRentSummary>> findAll(DSLContext create);


    /**
     * Makes the actual film renting
     *
     * @param create          the {@link DSLContext} with which to access and query the database using
     *                        the typesafe SQL Java DSL provided by JOOQ
     * @param newCustomerRent the customer films' rent details
     * @param rentDate        the date the films are rented
     * @return an empty future that, when successfully completed, means that the customer films were successfully rented
     */
    CompletionStage<Void> rentFilms(DSLContext create, NewCustomerFilmRentRequest newCustomerRent, LocalDate rentDate);

}
