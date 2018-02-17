package services;

import jooq.jooqobjects.tables.pojos.Customer;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface CustomerService {

    /**
     * Retrieves all the available {@link Customer}s
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of {@link Customer}s
     */
    CompletionStage<List<Customer>> findAll(DSLContext create);


    /**
     * Retrieves the {@link Customer} with the given customer code
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode the customer's code
     * @return a Future that, when redeemed, will contain the customer if it exists or an empty {@link Optional}
     * otherwise
     */
    CompletionStage<Optional<Customer>> findCustomerByCustomerCode(DSLContext create, String customerCode);


    /**
     * Retrieves the bonus points for the {@link Customer} with the given customer code
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the bonus points of the customer if it exists or an
     * empty {@link Optional} otherwise
     */
    CompletionStage<Optional<Integer>> findBonusPointsByCustomerCode(DSLContext create, String customercode);


    /**
     * Stores a new {@link Customer}
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerName the name of the new customer
     * @param balance      the customer's initial balance
     * @return an empty future that, when successfully completed, means that the customer was successfully stored
     */
    CompletionStage<Void> save(DSLContext create, String customerName, BigDecimal balance);

}
