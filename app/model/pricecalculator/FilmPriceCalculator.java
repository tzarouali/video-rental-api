package model.pricecalculator;

import exceptions.InvalidRequestInputException;
import model.FilmDetails;
import model.NewCustomerFilmReturn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Simple film price calculator using the Chain-of-responsibility pattern to factor out the calculation process to
 * each film type class
 */
public abstract class FilmPriceCalculator {
    protected FilmPriceCalculator nextCalculator;


    // The name of the film type. One of "New Release", "Regular film" or "Old film"
    protected abstract String getFilmTypeName();

    // the method that computes the film rental price for the given number of days depending on the type of film
    protected abstract BigDecimal doCalculateRentPrice(FilmDetails filmDetails, Integer numberRentDays);


    /**
     * Price computed by default for films of type "Regular film" or "Old film" for the number of given rent days
     */
    protected final BigDecimal doCalculateDefaultRentPrice(FilmDetails filmDetails, Integer numberRentDays) {
        if (numberRentDays <= filmDetails.getNumberDaysWithSamePrice()) {
            return filmDetails.getPrice();
        } else {
            BigDecimal basicPriceFirstDays = filmDetails.getPrice();
            Integer numberDaysAboveLimit = numberRentDays - filmDetails.getNumberDaysWithSamePrice();
            BigDecimal priceAboveLimit = filmDetails.getPrice().multiply(BigDecimal.valueOf(numberDaysAboveLimit));
            return priceAboveLimit.add(basicPriceFirstDays);
        }
    }


    /**
     * Sets the next price calculator.
     *
     * @param nextCalculator the next film price calculator
     */
    public void setNextPriceCalculator(FilmPriceCalculator nextCalculator) {
        this.nextCalculator = nextCalculator;
    }


    /**
     * Computes the rent for the film details and number of renting days given
     *
     * @param filmDetails    the film to rent and its details
     * @param numberRentDays the number of renting days
     * @return the price for renting the film for the given days
     */
    public BigDecimal calculateRentPrice(FilmDetails filmDetails, Integer numberRentDays) {
        if (getFilmTypeName().equals(filmDetails.getFilmTypeName())) {
            return doCalculateRentPrice(filmDetails, numberRentDays);
        } else if (nextCalculator != null) {
            return nextCalculator.calculateRentPrice(filmDetails, numberRentDays);
        } else {
            throw new InvalidRequestInputException("The film type cannot be found to compute the rent price");
        }
    }


    /**
     * Computes the price to charge the customer when returning a film after its supposed return date
     *
     * @param customerFilmReturn the film return details
     * @return the price to charge the customer when returning late. This will be zero if the film is return on its
     * supposed return date or earlier
     */
    public BigDecimal calculateLateReturnCharge(NewCustomerFilmReturn customerFilmReturn) {
        LocalDate finalDateToReturnFilm = customerFilmReturn.getRentDate().plus(customerFilmReturn.getNumberRentDays(), ChronoUnit.DAYS);
        BigDecimal lateChargePrice = BigDecimal.valueOf(0);
        if (customerFilmReturn.getActualReturnDate().isAfter(finalDateToReturnFilm)) {
            long daysExceeded = ChronoUnit.DAYS.between(finalDateToReturnFilm, customerFilmReturn.getActualReturnDate());
            return customerFilmReturn.getFilmTypePrice().multiply(BigDecimal.valueOf(daysExceeded));
        }
        return lateChargePrice;
    }
}
