import com.google.common.collect.ImmutableMap;
import jooq.jooqobjects.tables.pojos.Customer;
import org.apache.commons.lang3.StringUtils;
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
import services.CustomerService;
import services.impl.CustomerServiceImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class CustomerServiceTest extends WithApplication {

    private Database database;
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


    @Test
    public void testByDefaultNoCustomersAvailable() {
        DSLContext create = acquireJooqDslcontext();

        List<Customer> customers = customerService.findAll(create).toCompletableFuture().join();
        assertTrue(customers.isEmpty());
    }


    @Test
    public void testWhenOnlyOneCustomerIsAvailableOnlyOneCustomerCanBeRetrieved() {
        DSLContext create = acquireJooqDslcontext();

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        String customerName = "Philip";
        customerService.save(create, customerName, initialBalance).toCompletableFuture().join();

        List<Customer> customers = customerService.findAll(create).toCompletableFuture().join();
        assertTrue(!customers.isEmpty());
        assertTrue(customers.size() == 1);

        Customer onlyCustomer = customers.get(0);
        assertTrue(onlyCustomer.getName().equals(customerName));
        assertTrue(onlyCustomer.getAvailableMoney().equals(initialBalance));
        assertTrue(!StringUtils.isBlank(onlyCustomer.getCode()));

        Optional<Customer> maybeCustomerByCode = customerService.findCustomerByCustomerCode(
                create,
                onlyCustomer.getCode()
        ).toCompletableFuture().join();

        assertTrue(maybeCustomerByCode.isPresent());

        Customer customerByCode = maybeCustomerByCode.get();
        assertTrue(customerByCode.getName().equals(onlyCustomer.getName()));
        assertTrue(customerByCode.getCode().equals(onlyCustomer.getCode()));
        assertTrue(customerByCode.getAvailableMoney().equals(onlyCustomer.getAvailableMoney()));
        assertTrue(customerByCode.getBonusPoints().equals(onlyCustomer.getBonusPoints()));
    }


    @Test
    public void testWhenCreatingACustomerTheDefaultBonusPointsIsZero() {
        DSLContext create = acquireJooqDslcontext();

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        String customerName = "Sharon";
        customerService.save(create, customerName, initialBalance).toCompletableFuture().join();

        List<Customer> customers = customerService.findAll(create).toCompletableFuture().join();
        assertTrue(!customers.isEmpty());
        assertTrue(customers.size() == 1);

        Customer onlyCustomer = customers.get(0);
        assertTrue(onlyCustomer.getBonusPoints().equals(0));

        Optional<Integer> maybeBonusPoints = customerService.findBonusPointsByCustomerCode(create, onlyCustomer.getCode()).toCompletableFuture().join();
        assertTrue(maybeBonusPoints.isPresent());
        assertTrue(maybeBonusPoints.get().equals(0));
    }
}
