package repositories;

import jooq.jooqobjects.tables.pojos.CustomerFilmRent;
import model.CustomerFilmRentSummary;
import model.NewCustomerFilmRent;
import model.NewCustomerFilmReturn;
import org.jooq.DSLContext;

import java.util.List;

public interface CustomerRentRepository {

    /**
     * Retrieves all the available customer film rents in the form of {@link CustomerFilmRentSummary} objects from
     * the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of customer rented films
     */
    List<CustomerFilmRentSummary> findAll(DSLContext create);


    /**
     * Finds the films rented already by a customer within the ones with the given IDs passed as argument
     *
     * @param create  the {@link DSLContext} with which to access and query the database using
     *                the typesafe SQL Java DSL provided by JOOQ
     * @param filmIds the list of film identifiers
     * @return the list of film identifiers for films already rented
     */
    List<Integer> findRentedFilmsIdsById(DSLContext create, List<Integer> filmIds);


    /**
     * Finds the film ids rented already by the customer with the given code within the ones with the
     * given IDs passed as argument
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param filmIds      the list of film identifiers
     * @param customerCode the customer's code
     * @return the list of film identifiers for films already rented by the customer
     */
    List<Integer> findRentedFilmsIdsByIdAndCustomerCode(DSLContext create, List<Integer> filmIds, String customerCode);


    /**
     * Finds the {@link CustomerFilmRent}s rented already by the customer with the given code within the ones with the
     * given IDs passed as argument
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param filmIds      the list of film identifiers
     * @param customerCode the customer's code
     * @return the list of rented films by this customer from within the film ids passed as argument
     */
    List<CustomerFilmRent> findRentedFilmsByIdAndCustomerCode(DSLContext create, List<Integer> filmIds, String customerCode);


    /**
     * Saves the {@link CustomerFilmRent}s in the database
     *
     * @param create               the {@link DSLContext} with which to access and query the database using
     *                             the typesafe SQL Java DSL provided by JOOQ
     * @param newCustomerFilmRents the customer film rents
     */
    void saveCustomerRent(DSLContext create, List<NewCustomerFilmRent> newCustomerFilmRents);


    /**
     * Marks the {@link CustomerFilmRent}s associated with the customer with the given code as returned
     *
     * @param create              the {@link DSLContext} with which to access and query the database using
     *                            the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode        the customer's code
     * @param customerFilmReturns the detailed list of films to return including any possible late return charges
     */
    void markFilmsAsReturned(DSLContext create, String customerCode, List<NewCustomerFilmReturn> customerFilmReturns);

}
