package controllers;

import model.NewCustomerFilmRentRequest;
import model.NewCustomerFilmReturnRequest;
import play.libs.Json;
import play.mvc.Result;
import services.CustomerFilmRentService;
import services.CustomerFilmReturnService;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

public class CustomerRentController extends BaseController {

    private CustomerFilmRentService customerFilmRentService;
    private CustomerFilmReturnService customerFilmReturnService;


    @Inject
    public CustomerRentController(CustomerFilmRentService customerFilmRentService,
                                  CustomerFilmReturnService customerFilmReturnService) {
        this.customerFilmRentService = customerFilmRentService;
        this.customerFilmReturnService = customerFilmReturnService;
    }


    /**
     * Finds all the available {@link model.CustomerFilmRentSummary} records
     *
     * @return HTTP 200 with the Json object representing the list of customer rents or HTTP 500 in case there's
     * an unexpected error
     */
    public CompletionStage<Result> findAllCustomerFilmRents() {
        return withTransaction(ctx ->
                customerFilmRentService.findAll(ctx)
        ).thenApply(customerRents ->
                ok(Json.toJson(customerRents)));
    }


    /**
     * Parses a {@link NewCustomerFilmRentRequest} JSON object from the request body and makes the actual film rental
     *
     * @return HTTP 201 status code if the operation succedeed, HTTP 400 in case there's a
     * problem with the input or HTTP 500 in case of error
     */
    public CompletionStage<Result> rentFilms() {
        NewCustomerFilmRentRequest customerFilmRentRequest = Json.fromJson(request().body().asJson(), NewCustomerFilmRentRequest.class);
        return withTransaction(ctx ->
                customerFilmRentService.rentFilms(ctx, customerFilmRentRequest, LocalDate.now())
        ).thenApply(nothing ->
                created());
    }


    /**
     * Parses a {@link NewCustomerFilmReturnRequest} JSON object from the request body and makes the actual film return
     *
     * @return HTTP 201 status code if the operation succedeed, HTTP 400 in case there's a
     * problem with the input or HTTP 500 in case of error
     */
    public CompletionStage<Result> returnFilms() {
        NewCustomerFilmReturnRequest customerFilmReturnRequest = Json.fromJson(request().body().asJson(), NewCustomerFilmReturnRequest.class);
        return withTransaction(ctx ->
                customerFilmReturnService.returnFilms(ctx, customerFilmReturnRequest, LocalDate.now())
        ).thenApply(nothing ->
                created());
    }

}
