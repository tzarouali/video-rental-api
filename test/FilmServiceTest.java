import com.google.common.collect.ImmutableMap;
import exceptions.InvalidRequestInputException;
import jooq.jooqobjects.tables.pojos.Film;
import jooq.jooqobjects.tables.pojos.FilmType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.test.WithApplication;
import services.FilmService;
import services.FilmTypeService;
import services.impl.FilmServiceImpl;
import services.impl.FilmTypeServiceImpl;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class FilmServiceTest extends WithApplication {

    private Database database;
    private FilmService filmService;
    private FilmTypeService filmTypeService;


    @Before
    public void setupDatabase() {
        database = Databases.createFrom(
                "video_rental_db",
                "org.h2.Driver",
                "jdbc:h2:mem:video_rental_db;INIT=RUNSCRIPT FROM 'classpath:/test_db_init.sql';MODE=PostgreSQL;DATABASE_TO_UPPER=FALSE",
                ImmutableMap.of(
                        "user", "rest_api_user",
                        "password", "rest_api_user"
                )
        );

        startPlay();

        filmService = app.injector().instanceOf(FilmServiceImpl.class);
        filmTypeService = app.injector().instanceOf(FilmTypeServiceImpl.class);
    }


    @After
    public void shutdownDatabase() {
        database.shutdown();
        stopPlay();
    }


    private DSLContext acquireJooqDslcontext() {
        Connection connection = database.getConnection();
        return DSL.using(connection, SQLDialect.H2, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS));
    }


    @Test
    public void testByDefaultNoFilmsAvailable() {
        List<Film> films = filmService.findAll(acquireJooqDslcontext()).toCompletableFuture().join();
        assertTrue(films.isEmpty());
    }


    @Test
    public void testWhenOnlyOneFilmIsAvailableOnlyOneFilmCanBeRetrieved() {
        DSLContext create = acquireJooqDslcontext();

        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, "New Release").toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());

        String filmName = "My first film of type New Release!";
        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();

        List<Film> allFilms = filmService.findAll(create).toCompletableFuture().join();
        assertTrue(!allFilms.isEmpty());
        assertTrue(allFilms.size() == 1);
        assertTrue(allFilms.get(0).getName().equals(filmName));
        assertTrue(allFilms.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));

        List<Film> allRentedFilms = filmService.findAllRented(create).toCompletableFuture().join();
        assertTrue(allRentedFilms.isEmpty());

        List<Film> allNotRentedFilms = filmService.findAllNonRented(create).toCompletableFuture().join();
        assertTrue(!allNotRentedFilms.isEmpty());
        assertTrue(allNotRentedFilms.size() == 1);
        assertTrue(allNotRentedFilms.get(0).getName().equals(filmName));
        assertTrue(allNotRentedFilms.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));
    }


    @Test
    public void testWhenOnlyOneFilmOfTypeNewReleaseIsAvailableOnlyOneFilmCanBeRetrievedOfThatType() {
        DSLContext create = acquireJooqDslcontext();

        String filmTypeName = "New Release";
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, filmTypeName).toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());

        String filmName = "The newly released film!";

        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();

        List<Film> films = filmService.findAllByType(create, filmTypeName).toCompletableFuture().join();
        assertTrue(!films.isEmpty());
        assertTrue(films.size() == 1);
        assertTrue(films.get(0).getName().equals(filmName));
        assertTrue(films.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));

        List<Film> allRentedFilms = filmService.findAllRented(create).toCompletableFuture().join();
        assertTrue(allRentedFilms.isEmpty());

        List<Film> allNotRentedFilms = filmService.findAllNonRented(create).toCompletableFuture().join();
        assertTrue(!allNotRentedFilms.isEmpty());
        assertTrue(allNotRentedFilms.size() == 1);
        assertTrue(allNotRentedFilms.get(0).getName().equals(filmName));
        assertTrue(allNotRentedFilms.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));
    }


    @Test
    public void testWhenOnlyOneFilmOfRegularTypeIsAvailableOnlyOneFilmCanBeRetrievedOfThatType() {
        DSLContext create = acquireJooqDslcontext();

        String filmTypeName = "Regular film";
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, filmTypeName).toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());

        String filmName = "The film of regular type!";

        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();

        List<Film> films = filmService.findAllByType(create, filmTypeName).toCompletableFuture().join();
        assertTrue(!films.isEmpty());
        assertTrue(films.size() == 1);
        assertTrue(films.get(0).getName().equals(filmName));
        assertTrue(films.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));

        List<Film> allRentedFilms = filmService.findAllRented(create).toCompletableFuture().join();
        assertTrue(allRentedFilms.isEmpty());

        List<Film> allNotRentedFilms = filmService.findAllNonRented(create).toCompletableFuture().join();
        assertTrue(!allNotRentedFilms.isEmpty());
        assertTrue(allNotRentedFilms.size() == 1);
        assertTrue(allNotRentedFilms.get(0).getName().equals(filmName));
        assertTrue(allNotRentedFilms.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));
    }


    @Test
    public void testWhenOnlyOneFilmOfOldTypeIsAvailableOnlyOneFilmCanBeRetrievedOfThatType() {
        DSLContext create = acquireJooqDslcontext();

        String filmTypeName = "Old film";
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, filmTypeName).toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());

        String filmName = "The film of old type!";

        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();

        List<Film> films = filmService.findAllByType(create, filmTypeName).toCompletableFuture().join();
        assertTrue(!films.isEmpty());
        assertTrue(films.size() == 1);
        assertTrue(films.get(0).getName().equals(filmName));
        assertTrue(films.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));

        List<Film> allRentedFilms = filmService.findAllRented(create).toCompletableFuture().join();
        assertTrue(allRentedFilms.isEmpty());

        List<Film> allNotRentedFilms = filmService.findAllNonRented(create).toCompletableFuture().join();
        assertTrue(!allNotRentedFilms.isEmpty());
        assertTrue(allNotRentedFilms.size() == 1);
        assertTrue(allNotRentedFilms.get(0).getName().equals(filmName));
        assertTrue(allNotRentedFilms.get(0).getFilmTypeId().equals(maybeFilmType.get().getId()));
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testWhenCreatingFilmWithNotExistingFilmTypeAnErrorIsGenerated() throws Throwable {
        DSLContext create = acquireJooqDslcontext();

        String filmName = "The film that won't be created!";

        try {
            // only film type ids 1,2,3 exist, which correspond to New Release, Regular Film and Old film
            filmService.save(create, filmName, 100).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }
}
