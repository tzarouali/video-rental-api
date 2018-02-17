package model;

import java.math.BigDecimal;
import java.util.List;

public class FilmsRentalPriceDetails {

    private BigDecimal totalRentalPrice;
    private Integer totalRentalBonusPoints;
    private List<FilmIdWithRentPriceAndBonus> filmsWithPrices;


    public FilmsRentalPriceDetails(BigDecimal totalRentalPrice, List<FilmIdWithRentPriceAndBonus> filmsWithPrices, Integer totalRentalBonusPoints) {
        this.totalRentalPrice = totalRentalPrice;
        this.filmsWithPrices = filmsWithPrices;
        this.totalRentalBonusPoints = totalRentalBonusPoints;
    }


    public BigDecimal getTotalRentalPrice() {
        return totalRentalPrice;
    }


    public List<FilmIdWithRentPriceAndBonus> getFilmsWithPrices() {
        return filmsWithPrices;
    }


    public Integer getTotalRentalBonusPoints() {
        return totalRentalBonusPoints;
    }
}
