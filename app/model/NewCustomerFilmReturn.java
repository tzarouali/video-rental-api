package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NewCustomerFilmReturn {

    private Integer filmId;
    private LocalDate rentDate;
    private Integer numberRentDays;
    private LocalDate actualReturnDate;
    private BigDecimal filmTypePrice;
    private BigDecimal lateReturnCharge;


    public NewCustomerFilmReturn(Integer filmId, LocalDate rentDate, Integer numberRentDays, LocalDate actualReturnDate, BigDecimal filmTypePrice) {
        this.filmId = filmId;
        this.rentDate = rentDate;
        this.numberRentDays = numberRentDays;
        this.actualReturnDate = actualReturnDate;
        this.filmTypePrice = filmTypePrice;
    }


    public Integer getFilmId() {
        return filmId;
    }


    public LocalDate getRentDate() {
        return rentDate;
    }


    public Integer getNumberRentDays() {
        return numberRentDays;
    }


    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }


    public BigDecimal getFilmTypePrice() {
        return filmTypePrice;
    }


    public BigDecimal getLateReturnCharge() {
        return lateReturnCharge;
    }


    public void setLateReturnCharge(BigDecimal lateReturnCharge) {
        this.lateReturnCharge = lateReturnCharge;
    }
}
