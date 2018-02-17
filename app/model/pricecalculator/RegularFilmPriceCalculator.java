package model.pricecalculator;

import model.FilmDetails;

import java.math.BigDecimal;

public class RegularFilmPriceCalculator extends FilmPriceCalculator {

    @Override
    protected String getFilmTypeName() {
        return "Regular film";
    }


    @Override
    protected BigDecimal doCalculateRentPrice(FilmDetails filmDetails, Integer numberRentDays) {
        return doCalculateDefaultRentPrice(filmDetails, numberRentDays);
    }
}
