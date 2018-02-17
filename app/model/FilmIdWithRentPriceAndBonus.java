package model;

import java.math.BigDecimal;

public class FilmIdWithRentPriceAndBonus {

    private Integer filmId;
    private BigDecimal computedRentPrice;
    private Integer computedBonusPoints;


    public FilmIdWithRentPriceAndBonus(Integer filmId, BigDecimal computedRentPrice, Integer computedBonusPoints) {
        this.filmId = filmId;
        this.computedRentPrice = computedRentPrice;
        this.computedBonusPoints = computedBonusPoints;
    }


    public Integer getFilmId() {
        return filmId;
    }


    public BigDecimal getComputedRentPrice() {
        return computedRentPrice;
    }


    public Integer getComputedBonusPoints() {
        return computedBonusPoints;
    }
}
