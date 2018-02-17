package utils;

import model.pricecalculator.FilmPriceCalculator;
import model.pricecalculator.NewReleasePriceCalculator;
import model.pricecalculator.OldFilmPriceCalculator;
import model.pricecalculator.RegularFilmPriceCalculator;

/**
 * Simple utility class to prepare the film price calculator
 */
public class FilmPriceCalculatorUtils {

    private FilmPriceCalculatorUtils() {
    }


    /**
     * Prepares the film price calculator using the Chain-of-responsibility pattern by chaining the different film types
     * in the correct order.
     * Once the chain is prepared, we can call the methods: <br>
     * <code>calculateRentForDays(...)</code><br>
     * <code>calculateBonusPoints(...)</code><br>
     * <code>calculateLateReturnCharge(...)</code><br>
     */
    public static FilmPriceCalculator prepareFilmPriceCalculator() {
        NewReleasePriceCalculator newReleasePriceCalculator = new NewReleasePriceCalculator();
        RegularFilmPriceCalculator regularFilmPriceCalculator = new RegularFilmPriceCalculator();
        OldFilmPriceCalculator oldFilmPriceCalculator = new OldFilmPriceCalculator();

        // new releases have priority over regular releases
        newReleasePriceCalculator.setNextPriceCalculator(regularFilmPriceCalculator);
        // regular release have priority over old films
        regularFilmPriceCalculator.setNextPriceCalculator(oldFilmPriceCalculator);

        return newReleasePriceCalculator;
    }

}
