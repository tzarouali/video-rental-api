package services;

import jooq.jooqobjects.tables.pojos.Film;
import org.jooq.DSLContext;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface FilmService {

    /**
     * Retrieves all the available {@link Film}s
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of {@link Film}s
     */
    CompletionStage<List<Film>> findAll(DSLContext create);


    /**
     * Retrieves all the available {@link Film}s of a given type
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmType the film type
     * @return a Future that, when redeemed, will contain the list of {@link Film}s
     */
    CompletionStage<List<Film>> findAllByType(DSLContext create, String filmType);


    /**
     * Retrieves all the rented {@link Film}s
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of {@link Film}s
     */
    CompletionStage<List<Film>> findAllRented(DSLContext create);


    /**
     * Retrieves all the rented {@link Film}s of a given type
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmType the film type
     * @return a Future that, when redeemed, will contain the list of {@link Film}s
     */
    CompletionStage<List<Film>> findAllRentedByType(DSLContext create, String filmType);


    /**
     * Retrieves all the available {@link Film}s that can be rented
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of {@link Film}s
     */
    CompletionStage<List<Film>> findAllNonRented(DSLContext create);


    /**
     * Retrieves all the available {@link Film}s that can be rented by type
     *
     * @param create   the {@link DSLContext} with which to access and query the database using
     *                 the typesafe SQL Java DSL provided by JOOQ
     * @param filmType the film type
     * @return a Future that, when redeemed, will contain the list of {@link Film}s
     */
    CompletionStage<List<Film>> findAllNonRentedByType(DSLContext create, String filmType);


    /**
     * Stores a new {@link Film}
     *
     * @param create     the {@link DSLContext} with which to access and query the database using
     *                   the typesafe SQL Java DSL provided by JOOQ
     * @param filmName   the new film's name
     * @param filmTypeId the new film's type ID
     * @return an empty future that, when successfully completed, means that the film was successfully stored
     */
    CompletionStage<Void> save(DSLContext create, String filmName, Integer filmTypeId);

}
