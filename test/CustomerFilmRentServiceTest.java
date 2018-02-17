import com.google.common.collect.ImmutableMap;
import exceptions.InvalidRequestInputException;
import jooq.jooqobjects.tables.pojos.Customer;
import jooq.jooqobjects.tables.pojos.FilmType;
import model.CustomerFilmRentSummary;
import model.NewCustomerFilmRentRequest;
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
import services.CustomerFilmRentService;
import services.CustomerService;
import services.FilmService;
import services.FilmTypeService;
import services.impl.CustomerFilmRentServiceImpl;
import services.impl.CustomerServiceImpl;
import services.impl.FilmServiceImpl;
import services.impl.FilmTypeServiceImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CustomerFilmRentServiceTest extends WithApplication {

    private Database database;
    private CustomerFilmRentService customerFilmRentService;
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


    private void createRegularFilm(DSLContext create, String filmName) {
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, "Regular film").toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());
        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();
    }


    private void createOldFilm(DSLContext create, String filmName) {
        Optional<FilmType> maybeFilmType = filmTypeService.findByName(create, "Old film").toCompletableFuture().join();
        assertTrue(maybeFilmType.isPresent());
        filmService.save(create, filmName, maybeFilmType.get().getId()).toCompletableFuture().join();
    }


    private Customer createCustomer(DSLContext create, BigDecimal initialBalance) {
        String customerNme = "John Malkovich";
        customerService.save(create, customerNme, initialBalance).toCompletableFuture().join();
        List<Customer> customers = customerService.findAll(create).toCompletableFuture().join();

        return customers.get(0);
    }


    private CustomerFilmRentSummary findRentSummaryByFilmId(List<CustomerFilmRentSummary> filmRentSummaries, Integer filmId) {
        return filmRentSummaries
                .stream()
                .filter(customerFilmRentSummary -> customerFilmRentSummary.getFilmId().equals(filmId))
                .findFirst()
                .get();
    }


    @Test
    public void testByDefaultNoFilmsAreRented() {
        DSLContext create = acquireJooqDslcontext();

        List<CustomerFilmRentSummary> rentSummaryList = customerFilmRentService.findAll(create).toCompletableFuture().join();
        assertTrue(rentSummaryList.isEmpty());
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testErrorIsGeneratedWhenRentingWithoutCustomerCode() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(null);
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testErrorIsGeneratedWhenNotPassingActualFilmIdsToRent() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(null);
        filmRentRequest.setNumberRentDays(1);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testErrorIsGeneratedWhenNotSpecifyingNumberOfRentDays() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(null);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testErrorIsGeneratedWhenSpecifyingNotExistingFilmId() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(2)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testErrorIsGeneratedWhenSpecifyingNotExistingCustomerCode() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode("not exiting customer code");
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testErrorIsGeneratedWhenSpecifyingLessThanOneNumberOfRentDays() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(-1);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test(expected = InvalidRequestInputException.class)
    public void testFilmCannotBeRentedWithoutEnoughCustomerFunds() throws Throwable {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(30); // new releases cost 40 SEK per day
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(1);

        try {
            customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();
        } catch (Exception e) {
            throw ExceptionUtils.getRootCause(e);
        }
    }


    @Test
    public void testWhenRentingFilmWithTypeNewReleaseForTwoDaysThePriceIsFourtyAndTwoBonusPointsAreGained() {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of type New Release!";
        createNewReleaseFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(80); // new releases cost 40 SEK per day
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(2);

        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        List<CustomerFilmRentSummary> filmRentSummaries = customerFilmRentService.findAll(dslContext).toCompletableFuture().join();
        assertTrue(!filmRentSummaries.isEmpty());
        assertTrue(filmRentSummaries.size() == 1);

        CustomerFilmRentSummary onlyAvailableRent = filmRentSummaries.get(0);
        assertTrue(onlyAvailableRent.getRentPrice().compareTo(BigDecimal.valueOf(80)) == 0);
        assertTrue(onlyAvailableRent.getCustomerName().equals(customer.getName()));
        assertTrue(onlyAvailableRent.getFilmId().equals(1)); // the first and only film created will have ID 1
        assertTrue(onlyAvailableRent.getNumberDaysRented().equals(2));
        assertTrue(onlyAvailableRent.getFilmName().equals(filmName));
        assertTrue(onlyAvailableRent.getRentDate().equals(LocalDate.now()));
        assertNull(onlyAvailableRent.getReturnDate());
        assertNull(onlyAvailableRent.getLateReturnCharge());
        assertTrue(onlyAvailableRent.getGainedBonusPoints().equals(2)); // new releases give 2 points per rental

        Optional<Customer> maybeCustomerUpdated = customerService.findCustomerByCustomerCode(dslContext, customer.getCode()).toCompletableFuture().join();
        assertTrue(maybeCustomerUpdated.isPresent());
        assertTrue(maybeCustomerUpdated.get().getAvailableMoney().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(maybeCustomerUpdated.get().getBonusPoints().equals(2)); // total bonus points
    }


    @Test
    public void testWhenRentingFilmWithTypeRegularForThreeDaysThePriceIsThirtyAndOneBonusPointIsGained() {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of regular type!";
        createRegularFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(30); // regular releases cost 30 SEK the first 3 days and the rest of the days it's 30 SEK per day
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(3);

        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        List<CustomerFilmRentSummary> filmRentSummaries = customerFilmRentService.findAll(dslContext).toCompletableFuture().join();
        assertTrue(!filmRentSummaries.isEmpty());
        assertTrue(filmRentSummaries.size() == 1);

        CustomerFilmRentSummary onlyAvailableRent = filmRentSummaries.get(0);
        assertTrue(onlyAvailableRent.getRentPrice().compareTo(BigDecimal.valueOf(30)) == 0);
        assertTrue(onlyAvailableRent.getCustomerName().equals(customer.getName()));
        assertTrue(onlyAvailableRent.getFilmId().equals(1)); // the first and only film created will have ID 1
        assertTrue(onlyAvailableRent.getNumberDaysRented().equals(3));
        assertTrue(onlyAvailableRent.getFilmName().equals(filmName));
        assertTrue(onlyAvailableRent.getRentDate().equals(LocalDate.now()));
        assertNull(onlyAvailableRent.getReturnDate());
        assertNull(onlyAvailableRent.getLateReturnCharge());
        assertTrue(onlyAvailableRent.getGainedBonusPoints().equals(1)); // regular releases give 1 point per rental


        Optional<Customer> maybeCustomerUpdated = customerService.findCustomerByCustomerCode(dslContext, customer.getCode()).toCompletableFuture().join();
        assertTrue(maybeCustomerUpdated.isPresent());
        assertTrue(maybeCustomerUpdated.get().getAvailableMoney().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(maybeCustomerUpdated.get().getBonusPoints().equals(1)); // total bonus points
    }


    @Test
    public void testWhenRentingFilmWithTypeOldForFiveDaysThePriceIsThirtyAndOneBonusPointIsGained() {
        DSLContext dslContext = acquireJooqDslcontext();

        String filmName = "My first film of old type!";
        createOldFilm(dslContext, filmName);

        BigDecimal initialBalance = BigDecimal.valueOf(30); // old releases cost 30 SEK the first 5 days and the rest of the days it's 30 SEK per day
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // the first and only film created will have ID 1
        filmRentRequest.setNumberRentDays(5);

        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        List<CustomerFilmRentSummary> filmRentSummaries = customerFilmRentService.findAll(dslContext).toCompletableFuture().join();
        assertTrue(!filmRentSummaries.isEmpty());
        assertTrue(filmRentSummaries.size() == 1);

        CustomerFilmRentSummary onlyAvailableRent = filmRentSummaries.get(0);
        assertTrue(onlyAvailableRent.getRentPrice().compareTo(BigDecimal.valueOf(30)) == 0);
        assertTrue(onlyAvailableRent.getCustomerName().equals(customer.getName()));
        assertTrue(onlyAvailableRent.getFilmId().equals(1)); // the first and only film created will have ID 1
        assertTrue(onlyAvailableRent.getNumberDaysRented().equals(5));
        assertTrue(onlyAvailableRent.getFilmName().equals(filmName));
        assertTrue(onlyAvailableRent.getRentDate().equals(LocalDate.now()));
        assertNull(onlyAvailableRent.getReturnDate());
        assertNull(onlyAvailableRent.getLateReturnCharge());
        assertTrue(onlyAvailableRent.getGainedBonusPoints().equals(1)); // old films give 1 point per rental


        Optional<Customer> maybeCustomerUpdated = customerService.findCustomerByCustomerCode(dslContext, customer.getCode()).toCompletableFuture().join();
        assertTrue(maybeCustomerUpdated.isPresent());
        assertTrue(maybeCustomerUpdated.get().getAvailableMoney().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(maybeCustomerUpdated.get().getBonusPoints().equals(1)); // total bonus points
    }


    @Test
    public void testRentingExampleFromProgrammingTestDescriptionWorks() {
        DSLContext dslContext = acquireJooqDslcontext();

        String matrix11 = "Matrix 11";
        createNewReleaseFilm(dslContext, matrix11); // ID 1

        String spiderman = "Spiderman";
        createRegularFilm(dslContext, spiderman); // ID 2

        String spiderman2 = "Spiderman 2";
        createRegularFilm(dslContext, spiderman2); // ID 3

        String outOfAfrica = "Out of Africa";
        createOldFilm(dslContext, outOfAfrica); // ID 4


        // the total rental price has to be 250 SEK
        BigDecimal initialBalance = BigDecimal.valueOf(300);
        Customer customer = createCustomer(dslContext, initialBalance);

        NewCustomerFilmRentRequest filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(1)); // Matrix 11
        filmRentRequest.setNumberRentDays(1);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(2)); // Spiderman
        filmRentRequest.setNumberRentDays(5);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(3)); // Spiderman 2
        filmRentRequest.setNumberRentDays(2);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        filmRentRequest = new NewCustomerFilmRentRequest();
        filmRentRequest.setCustomerCode(customer.getCode());
        filmRentRequest.setFilmIds(Collections.singletonList(4)); // Out of Africa
        filmRentRequest.setNumberRentDays(7);
        customerFilmRentService.rentFilms(dslContext, filmRentRequest, LocalDate.now()).toCompletableFuture().join();

        List<CustomerFilmRentSummary> filmRentSummaries = customerFilmRentService.findAll(dslContext).toCompletableFuture().join();
        assertTrue(!filmRentSummaries.isEmpty());
        assertTrue(filmRentSummaries.size() == 4);

        CustomerFilmRentSummary matrixRent = findRentSummaryByFilmId(filmRentSummaries, 1);
        assertTrue(matrixRent.getRentPrice().compareTo(BigDecimal.valueOf(40)) == 0);
        assertTrue(matrixRent.getCustomerName().equals(customer.getName()));
        assertTrue(matrixRent.getFilmId().equals(1));
        assertTrue(matrixRent.getNumberDaysRented().equals(1));
        assertTrue(matrixRent.getFilmName().equals(matrix11));
        assertTrue(matrixRent.getRentDate().equals(LocalDate.now()));
        assertNull(matrixRent.getReturnDate());
        assertNull(matrixRent.getLateReturnCharge());
        assertTrue(matrixRent.getGainedBonusPoints().equals(2)); // new releases give 2 bonus points

        CustomerFilmRentSummary spidermanRent = findRentSummaryByFilmId(filmRentSummaries, 2);
        assertTrue(spidermanRent.getRentPrice().compareTo(BigDecimal.valueOf(90)) == 0);
        assertTrue(spidermanRent.getCustomerName().equals(customer.getName()));
        assertTrue(spidermanRent.getFilmId().equals(2));
        assertTrue(spidermanRent.getNumberDaysRented().equals(5));
        assertTrue(spidermanRent.getFilmName().equals(spiderman));
        assertTrue(spidermanRent.getRentDate().equals(LocalDate.now()));
        assertNull(spidermanRent.getReturnDate());
        assertNull(spidermanRent.getLateReturnCharge());
        assertTrue(spidermanRent.getGainedBonusPoints().equals(1)); // regular films give 1 bonus point

        CustomerFilmRentSummary spiderman2Rent = findRentSummaryByFilmId(filmRentSummaries, 3);
        assertTrue(spiderman2Rent.getRentPrice().compareTo(BigDecimal.valueOf(30)) == 0);
        assertTrue(spiderman2Rent.getCustomerName().equals(customer.getName()));
        assertTrue(spiderman2Rent.getFilmId().equals(3));
        assertTrue(spiderman2Rent.getNumberDaysRented().equals(2));
        assertTrue(spiderman2Rent.getFilmName().equals(spiderman2));
        assertTrue(spiderman2Rent.getRentDate().equals(LocalDate.now()));
        assertNull(spiderman2Rent.getReturnDate());
        assertNull(spiderman2Rent.getLateReturnCharge());
        assertTrue(spiderman2Rent.getGainedBonusPoints().equals(1)); // regular films give 1 bonus point

        CustomerFilmRentSummary outOfAfricaRent = findRentSummaryByFilmId(filmRentSummaries, 4);
        assertTrue(outOfAfricaRent.getRentPrice().compareTo(BigDecimal.valueOf(90)) == 0);
        assertTrue(outOfAfricaRent.getCustomerName().equals(customer.getName()));
        assertTrue(outOfAfricaRent.getFilmId().equals(4));
        assertTrue(outOfAfricaRent.getNumberDaysRented().equals(7));
        assertTrue(outOfAfricaRent.getFilmName().equals(outOfAfrica));
        assertTrue(outOfAfricaRent.getRentDate().equals(LocalDate.now()));
        assertNull(outOfAfricaRent.getReturnDate());
        assertNull(outOfAfricaRent.getLateReturnCharge());
        assertTrue(outOfAfricaRent.getGainedBonusPoints().equals(1)); // old films give 1 bonus point

        Optional<Customer> maybeCustomerUpdated = customerService.findCustomerByCustomerCode(dslContext, customer.getCode()).toCompletableFuture().join();
        assertTrue(maybeCustomerUpdated.isPresent());
        assertTrue(maybeCustomerUpdated.get().getAvailableMoney().compareTo(BigDecimal.valueOf(50)) == 0); // 300 was the initial balance and 250 the total rental price
        assertTrue(maybeCustomerUpdated.get().getBonusPoints().equals(5)); // total bonus points
    }
}
