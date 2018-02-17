package services.impl;

import jooq.jooqobjects.tables.pojos.Customer;
import org.jooq.DSLContext;
import repositories.CustomerRepository;
import services.CustomerService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;


    @Inject
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public CompletionStage<List<Customer>> findAll(DSLContext create) {
        return CompletableFuture.supplyAsync(() -> customerRepository.findAll(create));
    }


    @Override
    public CompletionStage<Optional<Customer>> findCustomerByCustomerCode(DSLContext create, String customerCode) {
        return CompletableFuture.supplyAsync(() -> customerRepository.findCustomerByCustomerCode(create, customerCode));
    }


    @Override
    public CompletionStage<Optional<Integer>> findBonusPointsByCustomerCode(DSLContext create, String customercode) {
        return CompletableFuture.supplyAsync(() -> customerRepository.findBonusPointsByCustomerCode(create, customercode));
    }


    @Override
    public CompletionStage<Void> save(DSLContext create, String customerName, BigDecimal balance) {
        return CompletableFuture.runAsync(() -> {
            // the customer code will be a random UUID
            String customerCode = UUID.randomUUID().toString();
            customerRepository.save(create, customerName, customerCode, balance);
        });
    }
}
