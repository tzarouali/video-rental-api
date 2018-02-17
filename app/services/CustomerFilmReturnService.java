package services;

import model.NewCustomerFilmReturnRequest;
import org.jooq.DSLContext;

import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

public interface CustomerFilmReturnService {

    /**
     * Makes the actual film returning
     *
     * @param create                       the {@link DSLContext} with which to access and query the database using
     *                                     the typesafe SQL Java DSL provided by JOOQ
     * @param newCustomerFilmReturnRequest the rental return request with the customer and list of film ids to return
     * @param returnDate                   the date the films are returned
     * @return an empty future that, when successfully completed, means that the customer films were
     * successfully returned
     */
    CompletionStage<Void> returnFilms(DSLContext create, NewCustomerFilmReturnRequest newCustomerFilmReturnRequest, LocalDate returnDate);

}
