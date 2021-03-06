/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables.records;


import javax.annotation.Generated;

import jooq.jooqobjects.tables.FilmType;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class FilmTypeRecord extends UpdatableRecordImpl<FilmTypeRecord> implements Record5<Integer, String, Integer, Integer, Integer> {

    private static final long serialVersionUID = 687768686;

    /**
     * Setter for <code>video_rental_schema.film_type.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>video_rental_schema.film_type.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>video_rental_schema.film_type.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>video_rental_schema.film_type.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>video_rental_schema.film_type.number_days_with_same_price</code>.
     */
    public void setNumberDaysWithSamePrice(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>video_rental_schema.film_type.number_days_with_same_price</code>.
     */
    public Integer getNumberDaysWithSamePrice() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>video_rental_schema.film_type.default_bonus_points</code>.
     */
    public void setDefaultBonusPoints(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>video_rental_schema.film_type.default_bonus_points</code>.
     */
    public Integer getDefaultBonusPoints() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>video_rental_schema.film_type.price_type_id</code>.
     */
    public void setPriceTypeId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>video_rental_schema.film_type.price_type_id</code>.
     */
    public Integer getPriceTypeId() {
        return (Integer) get(4);
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
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Integer, String, Integer, Integer, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Integer, String, Integer, Integer, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return FilmType.FILM_TYPE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return FilmType.FILM_TYPE.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return FilmType.FILM_TYPE.NUMBER_DAYS_WITH_SAME_PRICE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return FilmType.FILM_TYPE.DEFAULT_BONUS_POINTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return FilmType.FILM_TYPE.PRICE_TYPE_ID;
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
    public Integer value3() {
        return getNumberDaysWithSamePrice();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getDefaultBonusPoints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getPriceTypeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilmTypeRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilmTypeRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilmTypeRecord value3(Integer value) {
        setNumberDaysWithSamePrice(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilmTypeRecord value4(Integer value) {
        setDefaultBonusPoints(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilmTypeRecord value5(Integer value) {
        setPriceTypeId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilmTypeRecord values(Integer value1, String value2, Integer value3, Integer value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached FilmTypeRecord
     */
    public FilmTypeRecord() {
        super(FilmType.FILM_TYPE);
    }

    /**
     * Create a detached, initialised FilmTypeRecord
     */
    public FilmTypeRecord(Integer id, String name, Integer numberDaysWithSamePrice, Integer defaultBonusPoints, Integer priceTypeId) {
        super(FilmType.FILM_TYPE);

        set(0, id);
        set(1, name);
        set(2, numberDaysWithSamePrice);
        set(3, defaultBonusPoints);
        set(4, priceTypeId);
    }
}
