import com.google.common.collect.ImmutableMap;
import exceptions.InvalidRequestInputException;
import jooq.jooqobjects.tables.pojos.Customer;
import jooq.jooqobjects.tables.pojos.FilmType;
import model.CustomerFilmRentSummary;
import model.NewCustomerFilmRentRequest;
import model.NewCustomerFilmReturnRequest;
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
import services.*;
import services.impl.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class CustomerFilmReturnServiceTest extends WithApplication {

    private Database database;
    private CustomerFilmRentService customerFilmRentService;
    private CustomerFilmReturnService customerFilmReturnService;
    private FilmTypeService filmTypeService;
    private FilmService filmService;
    private CustomerService customerService;


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

        customerFilmRentService = app.injector().instanceOf(CustomerFilmRentServiceImpl.class);
        customerFilmReturnService = app.injector().instanceOf(CustomerFilmReturnServiceImpl.class);
        filmTypeService = app.injector().instanceOf(FilmTypeServiceImpl.class);
        filmService = app.injector().instanceOf(FilmServiceImpl.class);
        customerService = app.injector().instanceOf(CustomerServiceImpl.class);
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


    private void createNewReleaseFilm(DSLContext create, String filmName) {
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, "New Release").toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());
        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();
    }


    private Customer createCustomer(DSLContext create, BigDecimal initialBalance) {
        String customerNme = "John Malkovich";
        customerService.save(create, customerNme, initialBalance).toCompletableFuture().join();
        List<Customer> customers = customerService.findAll(create).toCompletableFuture().join();

        return customers.get(0);
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testCannotReturnFilmsWithNotExistingCustomer() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        createNewReleaseFilm(dslContext, "The newly release film"); // ID 1

        NewCustomerFilmReturnRequest filmReturnRequest = new NewCustomerFilmReturnRequest();
        filmReturnRequest.setCustomerCode("not existing customer code");
        filmReturnRequest.setFilmIds(Collections.singletonList(1));

        try {
            customerFilmReturnService.returnFilms(dslContext, filmReturnRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testCannotReturnFilmsThatDoNotExist() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        Customer customer = createCustomer(dslContext, BigDecimal.valueOf(100));

        NewCustomerFilmReturnRequest filmReturnRequest = new NewCustomerFilmReturnRequest();
        filmReturnRequest.setCustomerCode(customer.getCode());
        filmReturnRequest.setFilmIds(Collections.singletonList(1));

        try {
            customerFilmReturnService.returnFilms(dslContext, filmReturnRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testCannotReturnFilmsWhenTheFilmIsNotRentedByTheCustomer() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        Customer customer = createCustomer(dslContext, BigDecimal.valueOf(100));
        createNewReleaseFilm(dslContext, "The newly release film"); // ID 1

        // do the actual renting
        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        // try to return another film
        NewCustomerFilmReturnRequest filmReturnRequest = new NewCustomerFilmReturnRequest();
        filmReturnRequest.setCustomerCode(customer.getCode());
        filmReturnRequest.setFilmIds(Collections.singletonList(2)); // the only rented film by this customer has ID 1

        try {
            customerFilmReturnService.returnFilms(dslContext, filmReturnRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test
    public void testWhenOneFilmIsRentedAndReturnedNoLateChargesAreAdded() {
        DSLContext dslContext = acquireJooqDslcontext();

        Customer customer = createCustomer(dslContext, BigDecimal.valueOf(100));
        createNewReleaseFilm(dslContext, "The newly release film"); // ID 1

        // do the actual renting
        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);
        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, yesterday).toCompletableFuture().join();

        // return the film actually rented
        NewCustomerFilmReturnRequest filmReturnRequest = new NewCustomerFilmReturnRequest();
        filmReturnRequest.setCustomerCode(customer.getCode());
        filmReturnRequest.setFilmIds(Collections.singletonList(1));

        LocalDate today = LocalDate.now();
        customerFilmReturnService.returnFilms(dslContext, filmReturnRequest, today).toCompletableFuture().join();

        List<CustomerFilmRentSummary> filmRentSummaries = customerFilmRentService.findAll(dslContext).toCompletableFuture().join();
        assertTrue(!filmRentSummaries.isEmpty());
        assertTrue(filmRentSummaries.size() == 1);
        assertTrue(filmRentSummaries.get(0).getReturnDate().equals(today));

        // no late return charges added
        assertTrue(filmRentSummaries.get(0).getLateReturnCharge().compareTo(BigDecimal.ZERO) == 0);
    }


    @Test
    public void testWhenOneNewReleaseFilmIsRentedAndReturnedOneDayLateThenWeChargeFourtySek() {
        DSLContext dslContext = acquireJooqDslcontext();

        Customer customer = createCustomer(dslContext, BigDecimal.valueOf(100));
        createNewReleaseFilm(dslContext, "The newly release film"); // ID 1

        // do the actual renting
        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);

        LocalDate twoDaysAgo = LocalDate.now().minus(2, ChronoUnit.DAYS);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, twoDaysAgo).toCompletableFuture().join();

        // return the film actually rented
        NewCustomerFilmReturnRequest filmReturnRequest = new NewCustomerFilmReturnRequest();
        filmReturnRequest.setCustomerCode(customer.getCode());
        filmReturnRequest.setFilmIds(Collections.singletonList(1));

        LocalDate today = LocalDate.now();
        customerFilmReturnService.returnFilms(dslContext, filmReturnRequest, today).toCompletableFuture().join();

        List<CustomerFilmRentSummary> filmRentSummaries = customerFilmRentService.findAll(dslContext).toCompletableFuture().join();
        assertTrue(!filmRentSummaries.isEmpty());
        assertTrue(filmRentSummaries.size() == 1);
        assertTrue(filmRentSummaries.get(0).getReturnDate().equals(today));

        // 40 SEK charged for late return
        assertTrue(filmRentSummaries.get(0).getLateReturnCharge().compareTo(BigDecimal.valueOf(40)) == 0);
    }

}
