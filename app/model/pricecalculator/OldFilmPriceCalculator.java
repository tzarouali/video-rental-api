package model.pricecalculator;

import model.FilmDetails;

import java.math.BigDecimal;

public class OldFilmPriceCalculator extends FilmPriceCalculator {

    @Override
    protected String getFilmTypeName() {
        return "Old film";
    }


    @Override
    protected BigDecimal doCalculateRentPrice(FilmDetails filmDetails, Integer numberRentDays) {
        return doCalculateDefaultRentPrice(filmDetails, numberRentDays);
    }
}
