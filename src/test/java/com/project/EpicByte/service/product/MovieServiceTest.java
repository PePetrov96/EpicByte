package com.project.EpicByte.service.product;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.MovieRepository;
import com.project.EpicByte.service.impl.productImpl.BookServiceImpl;
import com.project.EpicByte.service.impl.productImpl.MovieServiceImpl;
import com.project.EpicByte.service.productService.MovieService;
import com.project.EpicByte.service.productService.ProductImagesService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    private MovieService movieService;

    @Mock
    MovieRepository movieRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductImagesService productImagesService;

    @Mock
    private Breadcrumbs breadcrumbs;

    @Mock
    private Model model;

    private BindingResult bindingResult;

    private MovieAddDTO movieAddDTO;
    private Movie movie;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        FieldNamesGenerator fieldNamesGenerator = new FieldNamesGenerator(messageSource);

        this.movieService = new MovieServiceImpl(movieRepository,
                cartRepository,
                modelMapper,
                messageSource,
                breadcrumbs,
                fieldNamesGenerator,
                productImagesService);

        this.breadcrumbs = new Breadcrumbs(messageSource);
        this.bindingResult = mock(BindingResult.class);

        this.movieAddDTO = new MovieAddDTO();
        movieAddDTO.setProductImageUrl("testURK");
        movieAddDTO.setProductName("testName");
        movieAddDTO.setProductPrice(10.00);
        movieAddDTO.setDescription("testDescription");

        movieAddDTO.setGenre("testGenre");
        movieAddDTO.setCarrier("DVD");

        this.movie = modelMapper.map(movieAddDTO, Movie.class);
    }

    @Test
    public void test_displayProductAddMoviePage() {
        String actualPage = this.movieService.displayProductAddMoviePage(model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void test_handleProductAddMovie_success() {
        String actualPage = this.movieService.handleProductAddMovie(movieAddDTO, bindingResult, model);
        Assertions.assertEquals(DISPLAY_TEXT_HTML, actualPage);
    }

    @Test
    public void test_handleProductAddMovie_fail() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String actualPage = this.movieService.handleProductAddMovie(new MovieAddDTO(), bindingResult, model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void test_displayAllMoviesPage() {
        String actualPage = this.movieService.displayAllMoviesPage(this.model, "default");
        Assertions.assertEquals(PRODUCTS_ALL_HTML, actualPage);
    }

    @Test
    public void test_displayDetailedViewMoviePage() {
        when(movieRepository.findMovieById(any())).thenReturn(movie);
        String actualPage = this.movieService.displayDetailedViewMoviePage(UUID.randomUUID(), model);
        Assertions.assertEquals(PRODUCT_DETAILS_HTML, actualPage);
    }

    @Test
    public void test_deleteMovie() {
        when(movieRepository.findMovieById(any())).thenReturn(movie);
        when(productImagesService.removeImageURL(any())).thenReturn(true);
        String actualPage = this.movieService.deleteMovie(UUID.randomUUID());
        Assertions.assertEquals("redirect:" + ALL_MOVIES_URL, actualPage);
    }
}
