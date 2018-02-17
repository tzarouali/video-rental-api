package model;

import java.math.BigDecimal;

public class FilmDetails {

    private Integer filmId;
    private BigDecimal price;
    private Integer numberDaysWithSamePrice;
    private Integer defaultBonusPoints;
    private String filmTypeName;


    public FilmDetails(Integer filmId, BigDecimal price, Integer numberDaysWithSamePrice, String filmTypeName, Integer defaultBonusPoints) {
        this.filmId = filmId;
        this.price = price;
        this.numberDaysWithSamePrice = numberDaysWithSamePrice;
        this.filmTypeName = filmTypeName;
        this.defaultBonusPoints = defaultBonusPoints;
    }


    public Integer getFilmId() {
        return filmId;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public Integer getNumberDaysWithSamePrice() {
        return numberDaysWithSamePrice;
    }


    public String getFilmTypeName() {
        return filmTypeName;
    }


    public Integer getDefaultBonusPoints() {
        return defaultBonusPoints;
    }
}
