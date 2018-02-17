package repositories.impl;

import jooq.jooqobjects.tables.pojos.CustomerFilmRent;
import jooq.jooqobjects.tables.records.CustomerFilmRentRecord;
import model.CustomerFilmRentSummary;
import model.NewCustomerFilmRent;
import model.NewCustomerFilmReturn;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep6;
import repositories.CustomerRentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static jooq.jooqobjects.Tables.*;

public class CustomerRentRepositoryImpl implements CustomerRentRepository {

    @Override
    public List<CustomerFilmRentSummary> findAll(DSLContext create) {
        return create
                .select(CUSTOMER_FILM_RENT.FILM_ID.as("filmId"), CUSTOMER.NAME.as("customerName"),
                        FILM.NAME.as("filmName"), CUSTOMER_FILM_RENT.PRICE.as("rentPrice"),
                        CUSTOMER_FILM_RENT.LATE_RETURN_CHARGE.as("lateReturnCharge"),
                        CUSTOMER_FILM_RENT.NUMBER_RENT_DAYS.as("numberDaysRented"), CUSTOMER_FILM_RENT.RENT_DATE.as("rentDate"),
                        CUSTOMER_FILM_RENT.RETURN_DATE.as("returnDate"), CUSTOMER_FILM_RENT.GAINED_BONUS_POINTS.as("gainedBonusPoints"))
                .from(CUSTOMER_FILM_RENT)
                .innerJoin(CUSTOMER).on(CUSTOMER.CODE.eq(CUSTOMER_FILM_RENT.CUSTOMER_CODE))
                .innerJoin(FILM).on(FILM.ID.eq(CUSTOMER_FILM_RENT.FILM_ID))
                .fetchInto(CustomerFilmRentSummary.class);
    }


    @Override
    public List<Integer> findRentedFilmsIdsById(DSLContext create, List<Integer> filmIds) {
        return create
                .select(CUSTOMER_FILM_RENT.FILM_ID)
                .from(CUSTOMER_FILM_RENT)
                .where(CUSTOMER_FILM_RENT.FILM_ID.in(filmIds))
                .and(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())
                .fetchInto(Integer.class);
    }


    @Override
    public List<Integer> findRentedFilmsIdsByIdAndCustomerCode(DSLContext create, List<Integer> filmIds, String customerCode) {
        return create
                .select(CUSTOMER_FILM_RENT.FILM_ID)
                .from(CUSTOMER_FILM_RENT)
                .where(CUSTOMER_FILM_RENT.FILM_ID.in(filmIds))
                .and(CUSTOMER_FILM_RENT.CUSTOMER_CODE.eq(customerCode))
                .and(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())
                .fetchInto(Integer.class);
    }


    @Override
    public List<CustomerFilmRent> findRentedFilmsByIdAndCustomerCode(DSLContext create, List<Integer> filmIds, String customerCode) {
        return create
                .select()
                .from(CUSTOMER_FILM_RENT)
                .where(CUSTOMER_FILM_RENT.FILM_ID.in(filmIds))
                .and(CUSTOMER_FILM_RENT.CUSTOMER_CODE.eq(customerCode))
                .and(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())
                .fetchInto(CustomerFilmRent.class);
    }


    @Override
    public void saveCustomerRent(DSLContext create, List<NewCustomerFilmRent> newCustomerFilmRents) {
        InsertValuesStep6<CustomerFilmRentRecord, String, Integer, BigDecimal, Integer, LocalDate, Integer> insertStatement =
                create.insertInto(CUSTOMER_FILM_RENT)
                        .columns(CUSTOMER_FILM_RENT.CUSTOMER_CODE,
                                CUSTOMER_FILM_RENT.FILM_ID,
                                CUSTOMER_FILM_RENT.PRICE,
                                CUSTOMER_FILM_RENT.NUMBER_RENT_DAYS,
                                CUSTOMER_FILM_RENT.RENT_DATE,
                                CUSTOMER_FILM_RENT.GAINED_BONUS_POINTS);

        for (NewCustomerFilmRent customerRent : newCustomerFilmRents) {
            insertStatement.values(
                    customerRent.getCustomerCode(),
                    customerRent.getFilmId(),
                    customerRent.getPrice(),
                    customerRent.getNumberRentDays(),
                    customerRent.getRentDate(),
                    customerRent.getGainedBonusPoints());
        }

        insertStatement.execute();
    }


    @Override
    public void markFilmsAsReturned(DSLContext create, String customerCode, List<NewCustomerFilmReturn> customerFilmReturns) {
        for (NewCustomerFilmReturn customerFilmReturn : customerFilmReturns) {
            create.update(CUSTOMER_FILM_RENT)
                    .set(CUSTOMER_FILM_RENT.LATE_RETURN_CHARGE, customerFilmReturn.getLateReturnCharge())
                    .set(CUSTOMER_FILM_RENT.RETURN_DATE, customerFilmReturn.getActualReturnDate())
                    .where(CUSTOMER_FILM_RENT.CUSTOMER_CODE.eq(customerCode))
                    .and(CUSTOMER_FILM_RENT.FILM_ID.eq(customerFilmReturn.getFilmId()))
                    .and(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())
                    .execute();
        }
    }
}
