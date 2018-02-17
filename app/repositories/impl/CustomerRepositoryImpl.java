package repositories.impl;

import jooq.jooqobjects.tables.pojos.Customer;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import repositories.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static jooq.jooqobjects.Tables.CUSTOMER;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public List<Customer> findAll(DSLContext create) {
        return create
                .selectFrom(CUSTOMER)
                .orderBy(CUSTOMER.NAME)
                .fetchInto(Customer.class);
    }


    @Override
    public Optional<Customer> findCustomerByCustomerCode(DSLContext create, String customerCode) {
        return create
                .selectFrom(CUSTOMER)
                .where(CUSTOMER.CODE.eq(customerCode))
                .fetchOptionalInto(Customer.class);
    }


    @Override
    public Optional<BigDecimal> findAvailableMoneyByCustomerCode(DSLContext create, String customerCode) {
        return create
                .select(CUSTOMER.AVAILABLE_MONEY)
                .from(CUSTOMER)
                .where(CUSTOMER.CODE.eq(customerCode))
                .fetchOptionalInto(BigDecimal.class);
    }


    @Override
    public Optional<Integer> findBonusPointsByCustomerCode(DSLContext create, String customerCode) {
        return create
                .select(CUSTOMER.BONUS_POINTS)
                .from(CUSTOMER)
                .where(CUSTOMER.CODE.eq(customerCode))
                .fetchOptionalInto(Integer.class);
    }


    @Override
    public void updateCustomerBonusPointsAndBalance(DSLContext create, String customerCode, BigDecimal amount, int bonusPoints) {
        BigDecimal zeroBalance = BigDecimal.valueOf(0);
        create.update(CUSTOMER)
                .set(CUSTOMER.AVAILABLE_MONEY, DSL.decode()
                        .when(CUSTOMER.AVAILABLE_MONEY.minus(amount)
                                .lessThan(zeroBalance), zeroBalance)
                        .otherwise(CUSTOMER.AVAILABLE_MONEY.minus(amount)))
                .set(CUSTOMER.BONUS_POINTS, CUSTOMER.BONUS_POINTS.plus(bonusPoints))
                .where(CUSTOMER.CODE.eq(customerCode))
                .execute();
    }


    @Override
    public void updateCustomerBalance(DSLContext create, String customerCode, BigDecimal amount) {
        BigDecimal zeroBalance = BigDecimal.valueOf(0);
        create.update(CUSTOMER)
                .set(CUSTOMER.AVAILABLE_MONEY, DSL.decode()
                        .when(CUSTOMER.AVAILABLE_MONEY.minus(amount)
                                .lessThan(zeroBalance), zeroBalance)
                        .otherwise(CUSTOMER.AVAILABLE_MONEY.minus(amount)))
                .where(CUSTOMER.CODE.eq(customerCode))
                .execute();
    }


    @Override
    public void save(DSLContext create, String customerName, String customerCode, BigDecimal balance) {
        create.insertInto(CUSTOMER)
                .columns(CUSTOMER.NAME, CUSTOMER.CODE, CUSTOMER.AVAILABLE_MONEY)
                .values(customerName, customerCode, balance)
                .execute();
    }
}
