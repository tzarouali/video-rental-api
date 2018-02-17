package services.impl;

import jooq.jooqobjects.tables.pojos.Film;
import jooq.jooqobjects.tables.pojos.FilmType;
import model.NewFilmRequest;
import org.jooq.DSLContext;
import repositories.FilmRepository;
import repositories.FilmTypeRepository;
import services.FilmService;
import utils.Validation;
import utils.Validator;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;
    private FilmTypeRepository filmTypeRepository;


    @Inject
    public FilmServiceImpl(FilmRepository filmRepository,
                           FilmTypeRepository filmTypeRepository) {
        this.filmRepository = filmRepository;
        this.filmTypeRepository = filmTypeRepository;
    }


    @Override
    public CompletionStage<List<Film>> findAll(DSLContext create) {
        return CompletableFuture.supplyAsync(() -> filmRepository.findAll(create));
    }


    @Override
    public CompletionStage<List<Film>> findAllByType(DSLContext create, String filmType) {
        return CompletableFuture.supplyAsync(() -> filmRepository.findAllByType(create, filmType));
    }


    @Override
    public CompletionStage<List<Film>> findAllRented(DSLContext create) {
        return CompletableFuture.supplyAsync(() -> filmRepository.findAllRented(create));
    }


    @Override
    public CompletionStage<List<Film>> findAllRentedByType(DSLContext create, String filmType) {
        return CompletableFuture.supplyAsync(() -> filmRepository.findAllRentedByType(create, filmType));
    }


    @Override
    public CompletionStage<List<Film>> findAllNonRented(DSLContext create) {
        return CompletableFuture.supplyAsync(() -> filmRepository.findAllNonRented(create));
    }


    @Override
    public CompletionStage<List<Film>> findAllNonRentedByType(DSLContext create, String filmType) {
        return CompletableFuture.supplyAsync(() -> filmRepository.findAllNonRentedByType(create, filmType));
    }


    @Override
    public CompletionStage<Void> save(DSLContext create, String filmName, Integer filmTypeId) {
        return CompletableFuture.runAsync(() -> {
            Optional<FilmType> maybeFilmType = filmTypeRepository.findById(create, filmTypeId);
            Boolean filmAlreadyExists = filmRepository.exists(create, filmName);

            Validator.apply(
                    Validation.with(
                            maybeFilmType.isPresent(),
                            "There are no film types with id " + filmTypeId),
                    Validation.with(
                            !filmAlreadyExists,
                            "There is already a film named " + filmName)
            ).validate();

            filmRepository.save(create, filmName, filmTypeId);
        });
    }
}
