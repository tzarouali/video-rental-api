/*
 * This file is generated by jOOQ.
*/
package jooq.jooqobjects.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.annotation.Generated;


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
public class CustomerFilmRent implements Serializable {

    private static final long serialVersionUID = -152675727;

    private Integer    id;
    private String     customerCode;
    private Integer    filmId;
    private BigDecimal price;
    private Integer    gainedBonusPoints;
    private BigDecimal lateReturnCharge;
    private Integer    numberRentDays;
    private LocalDate  rentDate;
    private LocalDate  returnDate;

    public CustomerFilmRent() {}

    public CustomerFilmRent(CustomerFilmRent value) {
        this.id = value.id;
        this.customerCode = value.customerCode;
        this.filmId = value.filmId;
        this.price = value.price;
        this.gainedBonusPoints = value.gainedBonusPoints;
        this.lateReturnCharge = value.lateReturnCharge;
        this.numberRentDays = value.numberRentDays;
        this.rentDate = value.rentDate;
        this.returnDate = value.returnDate;
    }

    public CustomerFilmRent(
        Integer    id,
        String     customerCode,
        Integer    filmId,
        BigDecimal price,
        Integer    gainedBonusPoints,
        BigDecimal lateReturnCharge,
        Integer    numberRentDays,
        LocalDate  rentDate,
        LocalDate  returnDate
    ) {
        this.id = id;
        this.customerCode = customerCode;
        this.filmId = filmId;
        this.price = price;
        this.gainedBonusPoints = gainedBonusPoints;
        this.lateReturnCharge = lateReturnCharge;
        this.numberRentDays = numberRentDays;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getFilmId() {
        return this.filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getGainedBonusPoints() {
        return this.gainedBonusPoints;
    }

    public void setGainedBonusPoints(Integer gainedBonusPoints) {
        this.gainedBonusPoints = gainedBonusPoints;
    }

    public BigDecimal getLateReturnCharge() {
        return this.lateReturnCharge;
    }

    public void setLateReturnCharge(BigDecimal lateReturnCharge) {
        this.lateReturnCharge = lateReturnCharge;
    }

    public Integer getNumberRentDays() {
        return this.numberRentDays;
    }

    public void setNumberRentDays(Integer numberRentDays) {
        this.numberRentDays = numberRentDays;
    }

    public LocalDate getRentDate() {
        return this.rentDate;
    }

    public void setRentDate(LocalDate rentDate) {
        this.rentDate = rentDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CustomerFilmRent (");

        sb.append(id);
        sb.append(", ").append(customerCode);
        sb.append(", ").append(filmId);
        sb.append(", ").append(price);
        sb.append(", ").append(gainedBonusPoints);
        sb.append(", ").append(lateReturnCharge);
        sb.append(", ").append(numberRentDays);
        sb.append(", ").append(rentDate);
        sb.append(", ").append(returnDate);

        sb.append(")");
        return sb.toString();
    }
}
