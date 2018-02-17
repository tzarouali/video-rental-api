package model.pricecalculator;

import model.FilmDetails;

import java.math.BigDecimal;

public class NewReleasePriceCalculator extends FilmPriceCalculator {

    @Override
    protected String getFilmTypeName() {
        return "New Release";
    }


    @Override
    protected BigDecimal doCalculateRentPrice(FilmDetails filmDetails, Integer numberRentDays) {
        return filmDetails.getPrice().multiply(BigDecimal.valueOf(numberRentDays));
    }

}
