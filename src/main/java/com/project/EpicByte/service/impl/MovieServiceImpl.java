package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.MovieAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.repository.MovieRepository;
import com.project.EpicByte.service.MovieService;
import com.project.EpicByte.util.Breadcrumbs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.project.EpicByte.util.Constants.*;

@Service
public class MovieServiceImpl extends Breadcrumbs implements MovieService {
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    public String displayProductAddMoviePage(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("movie.text", null, locale);

        model.addAttribute("linkType", "movie");
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("product", new MovieAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("movie"));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMovie(MovieAddDTO movieAddDTO, BindingResult bindingResult, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("movie.text", null, locale);

        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "movie");


        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("movie"));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addMovieToDatabase(movieAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Movie added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllMoviesPage(Model model, String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("movie.text", null, locale);

        addProductBreadcrumb(model, null, productTypeMessage);
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "movies");

        List<Movie> movieList;

        if (sort == null) {
            movieList = this.movieRepository.findAll();
        } else if (sort.equals("lowest")) {
            movieList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            movieList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            movieList = getAllSortedAlphabetically();
        } else {
            movieList = this.movieRepository.findAll();
        }

        model.addAttribute("productList", movieList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewMoviePage(Long id, Model model) {
        Movie movie = movieRepository.findMovieById(id);

        addProductBreadcrumb(model, null, "Movie", movie.getProductName());
        model.addAttribute("product", movie);
        model.addAttribute("productDetails", null);

        return PRODUCT_DETAILS_HTML;
    }

    private List<Movie> getAllSortedByLowestPrice() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Movie> getAllSortedByHighestPrice() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Movie> getAllSortedAlphabetically() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }

    // Support methods
    private void addMovieToDatabase(MovieAddDTO movieAddDTO) {
        Movie movie = modelMapper.map(movieAddDTO, Movie.class);

        movie.setProductType(ProductTypeEnum.MOVIE);
        movie.setDateCreated(LocalDate.now());
        movie.setNewProduct(true);

        movieRepository.saveAndFlush(movie);
    }
}
