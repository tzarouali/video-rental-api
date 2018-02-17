package repositories.impl;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import jooq.jooqobjects.tables.pojos.Film;
import model.FilmDetails;
import org.jooq.DSLContext;
import repositories.FilmRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jooq.jooqobjects.Tables.*;
import static org.jooq.impl.DSL.select;

public class FilmRepositoryImpl implements FilmRepository {

    @Override
    public List<Film> findAll(DSLContext create) {
        return create
                .selectFrom(FILM)
                .fetchInto(Film.class);
    }


    @Override
    public List<Film> findAllByType(DSLContext create, String filmType) {
        return create
                .select(FILM.ID, FILM.NAME, FILM.FILM_TYPE_ID)
                .from(FILM)
                .innerJoin(FILM_TYPE).on(FILM_TYPE.ID.eq(FILM.FILM_TYPE_ID))
                .where(FILM_TYPE.NAME.eq(filmType))
                .fetchInto(Film.class);
    }


    @Override
    public List<Film> findAllRented(DSLContext create) {
        return create
                .select(FILM.ID, FILM.NAME, FILM.FILM_TYPE_ID)
                .from(FILM)
                .innerJoin(CUSTOMER_FILM_RENT).on(CUSTOMER_FILM_RENT.FILM_ID.eq(FILM.ID))
                .where(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())
                .fetchInto(Film.class);
    }


    @Override
    public List<Film> findAllRentedByType(DSLContext create, String filmType) {
        return create
                .select(FILM.ID, FILM.NAME, FILM.FILM_TYPE_ID)
                .from(FILM)
                .innerJoin(FILM_TYPE).on(FILM_TYPE.ID.eq(FILM.FILM_TYPE_ID))
                .innerJoin(CUSTOMER_FILM_RENT).on(CUSTOMER_FILM_RENT.FILM_ID.eq(FILM.ID))
                .where(FILM_TYPE.NAME.eq(filmType))
                .and(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())
                .fetchInto(Film.class);
    }


    @Override
    public List<Film> findAllNonRented(DSLContext create) {
        return create
                .select(FILM.ID, FILM.NAME, FILM.FILM_TYPE_ID)
                .from(FILM)
                .where(FILM.ID.notIn(
                        select(CUSTOMER_FILM_RENT.FILM_ID)
                                .from(CUSTOMER_FILM_RENT)
                                .where(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())))
                .fetchInto(Film.class);
    }


    @Override
    public List<Film> findAllNonRentedByType(DSLContext create, String filmType) {
        return create
                .select(FILM.ID, FILM.NAME, FILM.FILM_TYPE_ID)
                .from(FILM)
                .innerJoin(FILM_TYPE).on(FILM_TYPE.ID.eq(FILM.FILM_TYPE_ID))
                .where(FILM.ID.notIn(
                        select(CUSTOMER_FILM_RENT.FILM_ID)
                                .from(CUSTOMER_FILM_RENT)
                                .where(CUSTOMER_FILM_RENT.RETURN_DATE.isNull())))
                .and(FILM_TYPE.NAME.eq(filmType))
                .fetchInto(Film.class);
    }


    @Override
    public Boolean exists(DSLContext create, String filmName) {
        return create.fetchExists(
                create.selectOne()
                        .from(FILM)
                        .where(FILM.NAME.eq(filmName))
        );
    }


    @Override
    public List<Integer> findNotExistingFilmIds(DSLContext create, List<Integer> filmIds) {
        List<Integer> existingFilmIds = create
                .select(FILM.ID)
                .from(FILM)
                .where(FILM.ID.in(filmIds))
                .fetchInto(Integer.class);

        return new ArrayList<>(Collections2.filter(filmIds, Predicates.not(Predicates.in(existingFilmIds))));
    }


    @Override
    public void save(DSLContext create, String filmName, Integer filmTypeId) {
        create.insertInto(FILM)
                .columns(FILM.NAME, FILM.FILM_TYPE_ID)
                .values(filmName, filmTypeId)
                .execute();
    }


    @Override
    public FilmDetails findFilmDetailsById(DSLContext create, Integer filmId) {
        return create
                .select(FILM.ID, PRICE_TYPE.VALUE, FILM_TYPE.NUMBER_DAYS_WITH_SAME_PRICE, FILM_TYPE.NAME, FILM_TYPE.DEFAULT_BONUS_POINTS)
                .from(FILM)
                .innerJoin(FILM_TYPE).on(FILM_TYPE.ID.eq(FILM.FILM_TYPE_ID))
                .innerJoin(PRICE_TYPE).on(PRICE_TYPE.ID.eq(FILM_TYPE.PRICE_TYPE_ID))
                .where(FILM.ID.eq(filmId))
                .fetchOneInto(FilmDetails.class);
    }


    @Override
    public BigDecimal findFilmPricebyId(DSLContext create, Integer filmId) {
        return create
                .select(PRICE_TYPE.VALUE)
                .from(FILM)
                .innerJoin(FILM_TYPE).on(FILM_TYPE.ID.eq(FILM.FILM_TYPE_ID))
                .innerJoin(PRICE_TYPE).on(PRICE_TYPE.ID.eq(FILM_TYPE.PRICE_TYPE_ID))
                .where(FILM.ID.eq(filmId))
                .fetchOneInto(BigDecimal.class);
    }
}
