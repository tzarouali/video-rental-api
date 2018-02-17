package repositories.impl;

import jooq.jooqobjects.tables.pojos.FilmType;
import org.jooq.DSLContext;
import repositories.FilmTypeRepository;

import java.util.List;
import java.util.Optional;

import static jooq.jooqobjects.Tables.FILM_TYPE;

public class FilmTypeRepositoryImpl implements FilmTypeRepository {

    @Override
    public List<FilmType> findAll(DSLContext create) {
        return create
                .selectFrom(FILM_TYPE)
                .fetchInto(FilmType.class);
    }


    @Override
    public Optional<FilmType> findByName(DSLContext create, String filmTypeName) {
        return create
                .selectFrom(FILM_TYPE)
                .where(FILM_TYPE.NAME.eq(filmTypeName))
                .fetchOptionalInto(FilmType.class);
    }


    @Override
    public Optional<FilmType> findById(DSLContext create, Integer filmTypeId) {
        return create
                .selectFrom(FILM_TYPE)
                .where(FILM_TYPE.ID.eq(filmTypeId))
                .fetchOptionalInto(FilmType.class);
    }
}
