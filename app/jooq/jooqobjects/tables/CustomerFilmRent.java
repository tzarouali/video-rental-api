/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import jooq.jooqconverters.DateToLocalDateConverter;
import jooq.jooqobjects.Keys;
import jooq.jooqobjects.VideoRentalSchema;
import jooq.jooqobjects.tables.records.CustomerFilmRentRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CustomerFilmRent extends TableImpl<CustomerFilmRentRecord> {

    private static final long serialVersionUID = 1134303352;

    /**
     * The reference instance of <code>video_rental_schema.customer_film_rent</code>
     */
    public static final CustomerFilmRent CUSTOMER_FILM_RENT = new CustomerFilmRent();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CustomerFilmRentRecord> getRecordType() {
        return CustomerFilmRentRecord.class;
    }

    /**
     * The column <code>video_rental_schema.customer_film_rent.id</code>.
     */
    public final TableField<CustomerFilmRentRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('video_rental_schema.customer_film_rent_id_seq'::regclass)", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.customer_code</code>.
     */
    public final TableField<CustomerFilmRentRecord, String> CUSTOMER_CODE = createField("customer_code", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.film_id</code>.
     */
    public final TableField<CustomerFilmRentRecord, Integer> FILM_ID = createField("film_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.price</code>.
     */
    public final TableField<CustomerFilmRentRecord, BigDecimal> PRICE = createField("price", org.jooq.impl.SQLDataType.NUMERIC.nullable(false), this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.gained_bonus_points</code>.
     */
    public final TableField<CustomerFilmRentRecord, Integer> GAINED_BONUS_POINTS = createField("gained_bonus_points", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.late_return_charge</code>.
     */
    public final TableField<CustomerFilmRentRecord, BigDecimal> LATE_RETURN_CHARGE = createField("late_return_charge", org.jooq.impl.SQLDataType.NUMERIC, this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.number_rent_days</code>.
     */
    public final TableField<CustomerFilmRentRecord, Integer> NUMBER_RENT_DAYS = createField("number_rent_days", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>video_rental_schema.customer_film_rent.rent_date</code>.
     */
    public final TableField<CustomerFilmRentRecord, LocalDate> RENT_DATE = createField("rent_date", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "", new DateToLocalDateConverter());

    /**
     * The column <code>video_rental_schema.customer_film_rent.return_date</code>.
     */
    public final TableField<CustomerFilmRentRecord, LocalDate> RETURN_DATE = createField("return_date", org.jooq.impl.SQLDataType.DATE, this, "", new DateToLocalDateConverter());

    /**
     * Create a <code>video_rental_schema.customer_film_rent</code> table reference
     */
    public CustomerFilmRent() {
        this("customer_film_rent", null);
    }

    /**
     * Create an aliased <code>video_rental_schema.customer_film_rent</code> table reference
     */
    public CustomerFilmRent(String alias) {
        this(alias, CUSTOMER_FILM_RENT);
    }

    private CustomerFilmRent(String alias, Table<CustomerFilmRentRecord> aliased) {
        this(alias, aliased, null);
    }

    private CustomerFilmRent(String alias, Table<CustomerFilmRentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return VideoRentalSchema.VIDEO_RENTAL_SCHEMA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CustomerFilmRentRecord, Integer> getIdentity() {
        return Keys.IDENTITY_CUSTOMER_FILM_RENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CustomerFilmRentRecord> getPrimaryKey() {
        return Keys.CUSTOMER_FILM_RENT_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CustomerFilmRentRecord>> getKeys() {
        return Arrays.<UniqueKey<CustomerFilmRentRecord>>asList(Keys.CUSTOMER_FILM_RENT_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<CustomerFilmRentRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<CustomerFilmRentRecord, ?>>asList(Keys.CUSTOMER_FILM_RENT__CUSTOMER_FILM_RENT_CUSTOMER_CODE_FKEY, Keys.CUSTOMER_FILM_RENT__CUSTOMER_FILM_RENT_FILM_ID_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomerFilmRent as(String alias) {
        return new CustomerFilmRent(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CustomerFilmRent rename(String name) {
        return new CustomerFilmRent(name, null);
    }
}