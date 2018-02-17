package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerFilmRentSummary {

    private Integer filmId;
    private String customerName;
    private String filmName;
    private BigDecimal rentPrice;
    private BigDecimal lateReturnCharge;
    private Integer numberDaysRented;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private Integer gainedBonusPoints;


    public CustomerFilmRentSummary(Integer filmId, String customerName, String filmName, BigDecimal rentPrice,
                                   BigDecimal lateReturnCharge, Integer numberDaysRented,
                                   LocalDate rentDate, LocalDate returnDate, Integer gainedBonusPoints) {
        this.filmId = filmId;
        this.customerName = customerName;
        this.filmName = filmName;
        this.rentPrice = rentPrice;
        this.lateReturnCharge = lateReturnCharge;
        this.numberDaysRented = numberDaysRented;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.gainedBonusPoints = gainedBonusPoints;
    }


    public Integer getFilmId() {
        return filmId;
    }


    public String getCustomerName() {
        return customerName;
    }


    public String getFilmName() {
        return filmName;
    }


    public BigDecimal getRentPrice() {
        return rentPrice;
    }


    public BigDecimal getLateReturnCharge() {
        return lateReturnCharge;
    }


    public Integer getNumberDaysRented() {
        return numberDaysRented;
    }


    public LocalDate getRentDate() {
        return rentDate;
    }


    public LocalDate getReturnDate() {
        return returnDate;
    }


    public Integer getGainedBonusPoints() {
        return gainedBonusPoints;
    }
}
