package utils;

import exceptions.InvalidRequestInputException;
import exceptions.ZeroPriceComputationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import play.Logger;
import play.http.HttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Custom error handler used to deal with unexpected errors
 */
@Singleton
public class CustomErrorHandler implements HttpErrorHandler {


    /**
     * Handles unexpected client errors that escape from the ones by default handled by Play itself
     *
     * @param request    the HTTP request
     * @param statusCode the HTTP error status code
     * @param message    the error message
     * @return a {@link CompletableFuture} with the client error
     */
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(
                Results.status(statusCode, message)
        );
    }


    /**
     * Handles unexpected server errors
     *
     * @param request   the HTTP request
     * @param exception the unexpected exception
     * @return a {@link CompletableFuture} with an error message
     */
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        Logger.error("Error executing HTTP request", rootCause);

        if (rootCause instanceof InvalidRequestInputException) {
            return CompletableFuture.completedFuture(Results.badRequest(rootCause.getMessage()));

        } else if (rootCause instanceof NumberFormatException) {
            return CompletableFuture.completedFuture(Results.badRequest("Number format error"));

        } else if (rootCause instanceof ZeroPriceComputationException) {
            return CompletableFuture.completedFuture(Results.internalServerError("Error calculating the rent price"));

        } else {
            return CompletableFuture.completedFuture(Results.internalServerError("Unexpected error processing the request!"));
        }
    }

}
