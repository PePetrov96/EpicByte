package com.project.EpicByte.service.impl.productServices;

import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.MovieCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.repository.MovieRepository;
import com.project.EpicByte.service.productServices.MovieService;
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
import java.util.*;

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
        model.addAttribute("linkType", "movie");
        model.addAttribute("productType", getLocalizedText("movie.text"));
        model.addAttribute("product", new MovieAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("movie", false));
        model.addAttribute("enumsList", MovieCarrierEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMovie(MovieAddDTO movieAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", getLocalizedText("movie.text"));
        model.addAttribute("linkType", "movie");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("movie", false));
            model.addAttribute("enumsList", MovieCarrierEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addMovieToDatabase(movieAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Movie added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllMoviesPage(Model model, String sort) {
        addProductBreadcrumb(model, ALL_MOVIES_URL, "Movies");
        model.addAttribute("productType", getLocalizedText("movies.text"));
        model.addAttribute("productLinkType", "movie");
        model.addAttribute("linkType", "movies");

        List<Movie> movieList;

        if (sort == null) {
            movieList = getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            movieList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            movieList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            movieList = getAllSortedAlphabetically();
        } else {
            movieList = getAllSortedByIsNewProduct();
        }

        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));

        model.addAttribute("productList", movieList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewMoviePage(UUID id, Model model) {
        Movie movie = movieRepository.findMovieById(id);

        addProductBreadcrumb(model, ALL_MOVIES_URL, "Movies", movie.getProductName());
        model.addAttribute("product", movie);
        model.addAttribute("productDetails", getDetailFields(movie));

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
    private Map<String, String> getDetailFields(Movie movie) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("genre.text"), movie.getGenre());
        fieldsMap.put(getLocalizedText("carrier.text"), movie.getCarrier().toString());

        return fieldsMap;
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private List<Movie> getAllSortedByIsNewProduct() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC,"isNewProduct"));
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

    private void addMovieToDatabase(MovieAddDTO movieAddDTO) {
        Movie movie = modelMapper.map(movieAddDTO, Movie.class);

        movie.setProductType(ProductTypeEnum.MOVIE);
        movie.setDateCreated(LocalDate.now());
        movie.setNewProduct(true);

        movieRepository.saveAndFlush(movie);
    }
}
