package utils;

import com.google.inject.AbstractModule;
import repositories.*;
import repositories.impl.*;
import services.*;
import services.impl.*;

public class GuiceDependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FilmRepository.class).to(FilmRepositoryImpl.class);
        bind(FilmService.class).to(FilmServiceImpl.class);

        bind(FilmTypeRepository.class).to(FilmTypeRepositoryImpl.class);
        bind(FilmTypeService.class).to(FilmTypeServiceImpl.class);

        bind(CustomerRepository.class).to(CustomerRepositoryImpl.class);
        bind(CustomerService.class).to(CustomerServiceImpl.class);

        bind(CustomerRentRepository.class).to(CustomerRentRepositoryImpl.class);
        bind(CustomerFilmRentService.class).to(CustomerFilmRentServiceImpl.class);

        bind(CustomerFilmReturnService.class).to(CustomerFilmReturnServiceImpl.class);
    }

}
