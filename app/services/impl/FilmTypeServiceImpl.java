package services.impl;

import jooq.jooqobjects.tables.pojos.FilmType;
import org.jooq.DSLContext;
import repositories.FilmTypeRepository;
import services.FilmTypeService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class FilmTypeServiceImpl implements FilmTypeService {

    private FilmTypeRepository filmTypeRepository;


    @Inject
    public FilmTypeServiceImpl(FilmTypeRepository filmTypeRepository) {
        this.filmTypeRepository = filmTypeRepository;
    }


    @Override
    public CompletionStage<List<FilmType>> findAll(DSLContext create) {
        return CompletableFuture.supplyAsync(() -> filmTypeRepository.findAll(create));
    }


    @Override
    public CompletionStage<Optional<FilmType>> findByName(DSLContext create, String filmTypeName) {
        return CompletableFuture.supplyAsync(() -> filmTypeRepository.findByName(create, filmTypeName));
    }
}
