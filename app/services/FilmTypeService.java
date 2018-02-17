package services;

import jooq.jooqobjects.tables.pojos.FilmType;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface FilmTypeService {

    /**
     * Retrieves all the available {@link FilmType}s
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return a Future that, when redeemed, will contain the list of {@link FilmType}s
     */
    CompletionStage<List<FilmType>> findAll(DSLContext create);


    /**
     * Retrieves the {@link FilmType} with the given name
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param filmTypeName the name of the film type
     * @return a Future that, when redeemed, wil contain the {@link FilmType} if it exists or an empty
     * {@link Optional} otherwise
     */
    CompletionStage<Optional<FilmType>> findByName(DSLContext create, String filmTypeName);

}
