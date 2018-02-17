package controllers;

import jooq.jooqobjects.tables.pojos.FilmType;
import play.libs.Json;
import play.mvc.Result;
import services.FilmTypeService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class FilmTypeController extends BaseController {

    private FilmTypeService filmTypeService;


    @Inject
    public FilmTypeController(FilmTypeService filmTypeService) {
        this.filmTypeService = filmTypeService;
    }


    /**
     * Finds all the available {@link FilmType}s
     *
     * @return HTTP 200 with the Json object representing the list of film types or HTTP 500 in case there's an
     * unexpected error
     */
    public CompletionStage<Result> findAllFilmTypes() {
        return withTransaction(ctx ->
                filmTypeService.findAll(ctx)
        ).thenApply(filmType ->
                ok(Json.toJson(filmType)));
    }

}
