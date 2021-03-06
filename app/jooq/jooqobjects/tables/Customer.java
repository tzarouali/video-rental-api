/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import jooq.jooqobjects.Keys;
import jooq.jooqobjects.VideoRentalSchema;
import jooq.jooqobjects.tables.records.CustomerRecord;

import org.jooq.Field;
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
public class Customer extends TableImpl<CustomerRecord> {

    private static final long serialVersionUID = -122417959;

    /**
     * The reference instance of <code>video_rental_schema.customer</code>
     */
    public static final Customer CUSTOMER = new Customer();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CustomerRecord> getRecordType() {
        return CustomerRecord.class;
    }

    /**
     * The column <code>video_rental_schema.customer.code</code>.
     */
    public final TableField<CustomerRecord, String> CODE = createField("code", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>video_rental_schema.customer.name</code>.
     */
    public final TableField<CustomerRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>video_rental_schema.customer.bonus_points</code>.
     */
    public final TableField<CustomerRecord, Integer> BONUS_POINTS = createField("bonus_points", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>video_rental_schema.customer.available_money</code>.
     */
    public final TableField<CustomerRecord, BigDecimal> AVAILABLE_MONEY = createField("available_money", org.jooq.impl.SQLDataType.NUMERIC.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.NUMERIC)), this, "");

    /**
     * Create a <code>video_rental_schema.customer</code> table reference
     */
    public Customer() {
        this("customer", null);
    }

    /**
     * Create an aliased <code>video_rental_schema.customer</code> table reference
     */
    public Customer(String alias) {
        this(alias, CUSTOMER);
    }

    private Customer(String alias, Table<CustomerRecord> aliased) {
        this(alias, aliased, null);
    }

    private Customer(String alias, Table<CustomerRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<CustomerRecord> getPrimaryKey() {
        return Keys.CUSTOMER_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CustomerRecord>> getKeys() {
        return Arrays.<UniqueKey<CustomerRecord>>asList(Keys.CUSTOMER_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer as(String alias) {
        return new Customer(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Customer rename(String name) {
        return new Customer(name, null);
    }
}
