package repositories;

import jooq.jooqobjects.tables.pojos.Customer;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    /**
     * Retrieves all the available {@link Customer}s from the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of {@link Customer}s
     */
    List<Customer> findAll(DSLContext create);


    /**
     * Retrieves the {@link Customer} with the given code
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode the customer's code
     * @return the customer entry if it exists or an empty {@link Optional} otherwise
     */
    Optional<Customer> findCustomerByCustomerCode(DSLContext create, String customerCode);


    /**
     * Retrieves the available money for the {@link Customer} with the given code
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode the customer's code
     * @return the available money for the {@link Customer} or an empty {@link Optional} it it doesn't exist
     */
    Optional<BigDecimal> findAvailableMoneyByCustomerCode(DSLContext create, String customerCode);


    /**
     * Retrieves the bonus points for the {@link Customer} with the given customer code
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode the customer's code
     * @return the bonus points of the customer if it exists or an empty {@link Optional} otherwise
     */
    Optional<Integer> findBonusPointsByCustomerCode(DSLContext create, String customerCode);


    /**
     * Updates the {@link Customer} balance subtracting from the available money the amount parameter
     * and updates the bonus points
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode the customer's code
     * @param amount       the amount to subtract from the customer's available funds
     * @param bonusPoints  the bonus points gained
     */
    void updateCustomerBonusPointsAndBalance(DSLContext create, String customerCode, BigDecimal amount, int bonusPoints);


    /**
     * Updates the {@link Customer} balance subtracting from the available money the amount passed
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerCode the customer's code
     * @param amount       the amount to subtract from the customer's available funds
     */
    void updateCustomerBalance(DSLContext create, String customerCode, BigDecimal amount);


    /**
     * Stores a new {@link Customer} in the database
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param customerName the name of the new customer
     * @param customerCode the code of the new customer
     * @param balance      the initial balance for this customer
     */
    void save(DSLContext create, String customerName, String customerCode, BigDecimal balance);

}
