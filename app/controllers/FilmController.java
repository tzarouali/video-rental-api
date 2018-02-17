package controllers;

import jooq.jooqobjects.tables.pojos.Film;
import play.libs.Json;
import play.mvc.Result;
import services.FilmService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class FilmController extends BaseController {

    private FilmService filmService;


    @Inject
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    /**
     * Finds all the available {@link Film}s
     *
     * @return HTTP 200 with the Json object representing the list of films or HTTP 500 in case there's an unexpected
     * error
     */
    public CompletionStage<Result> findAllFilms() {
        return withTransaction(ctx ->
                filmService.findAll(ctx)
        ).thenApply(films ->
                ok(Json.toJson(films)));
    }


    /**
     * Finds all the available {@link Film}s by type
     *
     * @param filmType the film type
     * @return HTTP 200 with the Json object representing the list of films or HTTP 500 in case there's an unexpected
     * error
     */
    public CompletionStage<Result> findAllFilmsByType(String filmType) {
        return withTransaction(ctx ->
                filmService.findAllByType(ctx, filmType)
        ).thenApply(films ->
                ok(Json.toJson(films)));
    }


    /**
     * Finds all the rented {@link Film}s
     *
     * @return HTTP 200 with the Json object representing the list of films or HTTP 500 in case there's an unexpected
     * error
     */
    public CompletionStage<Result> findAllRentedFilms() {
        return withTransaction(ctx ->
                filmService.findAllRented(ctx)
        ).thenApply(films ->
                ok(Json.toJson(films)));
    }


    /**
     * Finds all the rented {@link Film}s by type
     *
     * @param filmType the film type
     * @return HTTP 200 with the Json object representing the list of films or HTTP 500 in case there's an unexpected
     * error
     */
    public CompletionStage<Result> findAllRentedFilmsByType(String filmType) {
        return withTransaction(ctx ->
                filmService.findAllRentedByType(ctx, filmType)
        ).thenApply(films ->
                ok(Json.toJson(films)));
    }


    /**
     * Finds all the available {@link Film}s that can be rented
     *
     * @return HTTP 200 with the Json object representing the list of films or HTTP 500 in case there's an unexpected
     * error
     */
    public CompletionStage<Result> findAllNonRentedFilms() {
        return withTransaction(ctx ->
                filmService.findAllNonRented(ctx)
        ).thenApply(films ->
                ok(Json.toJson(films)));
    }


    /**
     * Finds all the available {@link Film}s that can be rented by type
     *
     * @param filmType the film type
     * @return HTTP 200 with the Json object representing the list of films or HTTP 500 in case there's an unexpected
     * error
     */
    public CompletionStage<Result> findAllNonRentedFilmsByType(String filmType) {
        return withTransaction(ctx ->
                filmService.findAllNonRentedByType(ctx, filmType)
        ).thenApply(films ->
                ok(Json.toJson(films)));
    }


    /**
     * Stores a new {@link Film} with the given details
     *
     * @return HTTP 201 status code if the operation succedeed, HTTP 400 in case there's a
     * problem with the input or HTTP 500 in case of error
     */
    public CompletionStage<Result> saveFilm(String filmName, Integer filmTypeId) {
        return withTransaction(ctx ->
                filmService.save(ctx, filmName, filmTypeId)
        ).thenApply(nothing ->
                created());
    }
}
