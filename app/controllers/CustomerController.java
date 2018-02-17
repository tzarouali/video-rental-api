package controllers;

import jooq.jooqobjects.tables.pojos.Customer;
import play.libs.Json;
import play.mvc.Result;
import services.CustomerService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.CompletionStage;

public class CustomerController extends BaseController {

    private CustomerService customerService;


    @Inject
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    /**
     * Finds all the available {@link Customer}s
     *
     * @return HTTP 200 with the Json object representing the list of customers or HTTP 500 if there's
     * an unexpected error
     */
    public CompletionStage<Result> findAllCustomers() {
        return withTransaction(ctx ->
                customerService.findAll(ctx)
        ).thenApply(customers ->
                ok(Json.toJson(customers)));
    }


    /**
     * Finds the available {@link Customer} with the given customer code
     *
     * @return HTTP 200 response with a Json object representing the customer if it's found or HTTP 400 in case it's
     * not found
     */
    public CompletionStage<Result> findCustomerByCode(String customerCode) {
        return withTransaction(ctx ->
                customerService.findCustomerByCustomerCode(ctx, customerCode)
        ).thenApply(customer -> {
            if (customer.isPresent()) {
                return ok(Json.toJson(customer));
            } else {
                return notFound();
            }
        });
    }


    /**
     * Finds the bonus points for the {@link Customer} with the given customer code
     *
     * @return HTTP 200 response with a Json object with the amount of bonus points if the customer is found
     * or HTTP 400 if the customer is not found
     */
    public CompletionStage<Result> findBonusPointsByCustomerCode(String customercode) {
        return withTransaction(ctx ->
                customerService.findBonusPointsByCustomerCode(ctx, customercode)
        ).thenApply(bonusPoints -> {
            if (bonusPoints.isPresent()) {
                return ok(Json.toJson(bonusPoints));
            } else {
                return notFound();
            }
        });
    }


    /**
     * Stores a new {@link Customer} with the given name and balance
     *
     * @param customerName the name of the new customer
     * @param balance      the initial balance
     * @return HTTP 201 status code if the operation succedeed, HTTP 400 in case there's a
     * problem with the input or HTTP 500 in case of error
     */
    public CompletionStage<Result> saveCustomer(String customerName, String balance) {
        return withTransaction(ctx ->
                customerService.save(ctx, customerName, new BigDecimal(balance))
        ).thenApply(nothing ->
                created());
    }

}
