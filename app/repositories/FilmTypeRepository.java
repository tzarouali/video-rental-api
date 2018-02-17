package repositories;

import jooq.jooqobjects.tables.pojos.FilmType;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

public interface FilmTypeRepository {

    /**
     * Retrieves all the available {@link FilmType}s from the database
     *
     * @param create the {@link DSLContext} with which to access and query the database using
     *               the typesafe SQL Java DSL provided by JOOQ
     * @return the list of {@link FilmType}s
     */
    List<FilmType> findAll(DSLContext create);


    /**
     * Retrieves the {@link FilmType} with the given name from the database
     *
     * @param create       the {@link DSLContext} with which to access and query the database using
     *                     the typesafe SQL Java DSL provided by JOOQ
     * @param filmTypeName the name of the film type
     * @return the {@link FilmType} if it exists or an empty {@link Optional} otherwise
     */
    Optional<FilmType> findByName(DSLContext create, String filmTypeName);


    /**
     * Retrieves the {@link FilmType} with the given id from the database
     *
     * @param create     the {@link DSLContext} with which to access and query the database using
     *                   the typesafe SQL Java DSL provided by JOOQ
     * @param filmTypeId the film type's id
     * @return the {@link FilmType} if it exists or an empty {@link Optional} otherwise
     */
    Optional<FilmType> findById(DSLContext create, Integer filmTypeId);

}
