package com.project.EpicByte.service.impl.productImpl;

import com.project.EpicByte.exceptions.NoSuchProductException;
import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import com.project.EpicByte.model.entity.enums.MovieCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.MovieRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.MovieService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
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
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final Breadcrumbs breadcrumbs;
    private final FieldNamesGenerator fieldNamesGenerator;
    //CLOUDINARY
    private final ProductImagesService productImagesService;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository,
                            CartRepository cartRepository,
                            ModelMapper modelMapper,
                            MessageSource messageSource,
                            Breadcrumbs breadcrumbs,
                            FieldNamesGenerator fieldNamesGenerator,
                            ProductImagesService productImagesService) {
        this.movieRepository = movieRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.breadcrumbs = breadcrumbs;
        this.fieldNamesGenerator = fieldNamesGenerator;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddMoviePage(Model model) {
        addDefaultModelAttributesForAddAndHandle(model);
        model.addAttribute("product", new MovieAddDTO());
        model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("movie", false));
        model.addAttribute("enumsList", MovieCarrierEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMovie(MovieAddDTO movieAddDTO, BindingResult bindingResult, Model model) {
        addDefaultModelAttributesForAddAndHandle(model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("movie", false));
            model.addAttribute("enumsList", MovieCarrierEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addMovieToDatabase(movieAddDTO, model);
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllMoviesPage(Model model, String sort) {
        breadcrumbs.addProductBreadcrumb(model, ALL_MOVIES_URL, "Movies");
        addDefaultModelAttributesForAllAndDetailed(model);
        List<Movie> movieList = getSortedMovies(sort);
        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));
        model.addAttribute("productList", movieList);
        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewMoviePage(UUID id, Model model) {
        Movie movie = getMovieOrThrowException(id);
        breadcrumbs.addProductBreadcrumb(model, ALL_MOVIES_URL, "Movies", movie.getProductName());
        addDefaultModelAttributesForAllAndDetailed(model);
        model.addAttribute("product", movie);
        model.addAttribute("productDetails", getDetailFields(movie));
        model.addAttribute("linkType", "movies");
        return PRODUCT_DETAILS_HTML;
    }


    @Override
    public String deleteMovie(UUID id) {
        deleteMovieFromDatabase(id);
        return "redirect:" + ALL_MOVIES_URL;
    }

    // Support methods
    private Movie getMovieOrThrowException(UUID id) {
        Movie movie = movieRepository.findMovieById(id);
        if (movie == null) throw new NoSuchProductException();
        return movie;
    }

    private void addDefaultModelAttributesForAddAndHandle(Model model) {
        model.addAttribute("linkType", "movie");
        model.addAttribute("productType", getLocalizedText("movie.text"));
    }

    private void addDefaultModelAttributesForAllAndDetailed(Model model) {
        model.addAttribute("productType", getLocalizedText("movies.text"));
        model.addAttribute("productLinkType", "movie");
        model.addAttribute("linkType", "movies");
    }

    private List<Movie> getSortedMovies(String sort) {
        if (sort == null || sort.equals("default")) {
            return getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            return getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            return getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            return getAllSortedAlphabetically();
        } else {
            return getAllSortedByIsNewProduct();
        }
    }

    private void deleteMovieFromDatabase(UUID id) {
        Movie movie = movieRepository.findMovieById(id);

        // Remove the image from Cloudinary
        this.productImagesService.removeImageURL(movie.getProductImageUrl());

        // Remove the image from the repository
        this.movieRepository.delete(movie);

        // Remove the product from all user carts
        List<CartItem> cartItemList = this.cartRepository.findAllByProductId(id);
        this.cartRepository.deleteAll(cartItemList);
    }

    private Map<String, String> getDetailFields(Movie movie) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("genre.text"), movie.getGenre());
        fieldsMap.put(getLocalizedText("carrier.text"), movie.getCarrier().toString());

        return fieldsMap;
    }

    private void addMovieToDatabase(MovieAddDTO movieAddDTO, Model model) {
        Movie movie = modelMapper.map(movieAddDTO, Movie.class);
        setNewMovieDetails(movie, movieAddDTO);
        movieRepository.saveAndFlush(movie);
        setSuccessMessageToModel(model);
    }

    private void setNewMovieDetails(Movie movie, MovieAddDTO movieAddDTO) {
        movie.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                movieAddDTO.getProductImageUrl()));
        movie.setNewProduct(true);
        movie.setDateCreated(LocalDate.now());
        movie.setProductType(ProductTypeEnum.MOVIE);
    }

    private void setSuccessMessageToModel(Model model) {
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("movie.added.successfully.text"));
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
}
