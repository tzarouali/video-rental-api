package repositories;

import jooq.jooqobjects.tables.pojos.Film;
import model.FilmDetails;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;

public interface FilmRepository {

    /**
     * Retrieves all the available {@link Film}s from the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of {@link Film}s
     */
    List<Film> findAll(DSLContext create);


    /**
     * Retrieves all the available {@link Film}s of a given type from the database
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmType the film type
     * @return the list of {@link Film}s
     */
    List<Film> findAllByType(DSLContext create, String filmType);


    /**
     * Retrieves all the rented {@link Film}s from the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of {@link Film}s
     */
    List<Film> findAllRented(DSLContext create);


    /**
     * Retrieves all the rented {@link Film}s of a given type from the database
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmType the film type
     * @return the list of {@link Film}s
     */
    List<Film> findAllRentedByType(DSLContext create, String filmType);


    /**
     * Retrieves all the available {@link Film}s that can be rented from the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of {@link Film}s
     */
    List<Film> findAllNonRented(DSLContext create);


    /**
     * Retrieves all the available {@link Film}s of a given type that can be rented from the database
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmType the film type
     * @return the list of {@link Film}s
     */
    List<Film> findAllNonRentedByType(DSLContext create, String filmType);


    /**
     * Checks whether there is a {@link Film} with the given name
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmName the film's name
     * @return True if there is already a film with the given name and False otherwise
     */
    Boolean exists(DSLContext create, String filmName);


    /**
     * Finds the list of not existing {@link Film} ids from the ones passed as argument
     *
     * @param create  the {@link DSLContext} with which to access and query the database using
     *                the typesafe SQL Java DSL provided by JOOQ
     * @param filmIds the list of film identifiers to check for existance
     * @return the list of film ids that do not exist
     */
    List<Integer> findNotExistingFilmIds(DSLContext create, List<Integer> filmIds);


    /**
     * Stores a new {@link Film} in the database
     *
     * @param create     the {@link DSLContext} with which to access and query the database using
     *                   the typesafe SQL Java DSL provided by JOOQ
     * @param filmName   the new film's name
     * @param filmTypeId the film type's ID
     */
    void save(DSLContext create, String filmName, Integer filmTypeId);


    /**
     * Retrieves the details of the film for the given {@link Film} id
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @param filmId the film identifier
     * @return the film details including the price (which depends on its type) and the number of renting days
     * with the same price
     */
    FilmDetails findFilmDetailsById(DSLContext create, Integer filmId);


    /**
     * Retrieves the {@link Film}'s price, which depends on the type of film
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @param filmId the film identifier
     * @return the price for this type of film
     */
    BigDecimal findFilmPricebyId(DSLContext create, Integer filmId);
}
