import com.google.common.collect.ImmutableMap;
import jooq.jooqobjects.tables.pojos.FilmType;
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
import services.FilmTypeService;
import services.impl.FilmTypeServiceImpl;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class FilmTypeServiceTest extends WithApplication {

    private Database database;
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
    public void testDefaultFilmTypesAvailable() {
        List<FilmType> filmTypes = filmTypeService.findAll(acquireJooqDslcontext()).toCompletableFuture().join();
        List<String> defaultFilmTypes = Arrays.asList("New Release", "Regular film", "Old film");
        assertTrue(filmTypes.size() == 3);
        assertArrayEquals(defaultFilmTypes.toArray(), filmTypes.stream().map(FilmType::getName).toArray());
    }


    @Test
    public void testOnlyFilmTypeIdsOneAndTwoAndThreeAreAvailable() {
        List<FilmType> filmTypes = filmTypeService.findAll(acquireJooqDslcontext()).toCompletableFuture().join();
        List<Integer> defaultFilmTypeIds = Arrays.asList(1, 2, 3);
        assertTrue(filmTypes.size() == 3);
        assertArrayEquals(defaultFilmTypeIds.toArray(), filmTypes.stream().map(FilmType::getId).toArray());
    }


    @Test
    public void testWhenLookingForNotExistingFilmTypeNameNothingIsReturned() {
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(acquireJooqDslcontext(), "No existing type!").toCompletableFuture().join();
        assertTrue(!maybeFilmType.isPresent());
    }
}
