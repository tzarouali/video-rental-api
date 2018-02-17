/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;

import jooq.jooqobjects.tables.PriceType;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


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
public class PriceTypeRecord extends UpdatableRecordImpl<PriceTypeRecord> implements Record3<Integer, String, BigDecimal> {

    private static final long serialVersionUID = 2012951127;

    /**
     * Setter for <code>video_rental_schema.price_type.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>video_rental_schema.price_type.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>video_rental_schema.price_type.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>video_rental_schema.price_type.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>video_rental_schema.price_type.value</code>.
     */
    public void setValue(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>video_rental_schema.price_type.value</code>.
     */
    public BigDecimal getValue() {
        return (BigDecimal) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, String, BigDecimal> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Integer, String, BigDecimal> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return PriceType.PRICE_TYPE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return PriceType.PRICE_TYPE.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return PriceType.PRICE_TYPE.VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PriceTypeRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PriceTypeRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PriceTypeRecord value3(BigDecimal value) {
        setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PriceTypeRecord values(Integer value1, String value2, BigDecimal value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PriceTypeRecord
     */
    public PriceTypeRecord() {
        super(PriceType.PRICE_TYPE);
    }

    /**
     * Create a detached, initialised PriceTypeRecord
     */
    public PriceTypeRecord(Integer id, String name, BigDecimal value) {
        super(PriceType.PRICE_TYPE);

        set(0, id);
        set(1, name);
        set(2, value);
    }
}