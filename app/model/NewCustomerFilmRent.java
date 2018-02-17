package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewCustomerFilmRent {

    private String customerCode;
    private Integer filmId;
    private BigDecimal price;
    private Integer gainedBonusPoints;
    private Integer numberRentDays;
    private LocalDate rentDate;


    public NewCustomerFilmRent(String customerCode, Integer filmId, BigDecimal price, Integer gainedBonusPoints, Integer numberRentDays, LocalDate rentDate) {
        this.customerCode = customerCode;
        this.filmId = filmId;
        this.price = price;
        this.gainedBonusPoints = gainedBonusPoints;
        this.numberRentDays = numberRentDays;
        this.rentDate = rentDate;
    }


    public String getCustomerCode() {
        return customerCode;
    }


    public Integer getFilmId() {
        return filmId;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public Integer getGainedBonusPoints() {
        return gainedBonusPoints;
    }


    public Integer getNumberRentDays() {
        return numberRentDays;
    }


    public LocalDate getRentDate() {
        return rentDate;
    }
}
