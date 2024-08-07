package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import com.project.EpicByte.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Display detailed single Movie entity page
    @SlowExecutionWarning
    @GetMapping("/movie" + PRODUCT_DETAILS_URL + "/{id}")
    public String viewMovieProductDetails(@PathVariable UUID id, Model model) {
        return movieService.displayDetailedViewMoviePage(id, model);
    }

    // Display all Movies page
    @SlowExecutionWarning
    @GetMapping(ALL_MOVIES_URL)
    public String displayMoviePage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.movieService.displayAllMoviesPage(model, sort);
    }

    // Display Add Movie page
    @SlowExecutionWarning
    @GetMapping(MODERATOR_PRODUCT_ADD_MOVIE_URL)
    public String displayProductAddMoviePage(Model model) {
        return movieService.displayProductAddMoviePage(model);
    }

    // Process Add new Movie
    @SlowExecutionWarning
    @PostMapping(MODERATOR_PRODUCT_ADD_MOVIE_URL)
    public String handleProductAddMoviePage(@Valid @ModelAttribute("product") MovieAddDTO musicAddDTO, BindingResult bindingResult, Model model) {
        return movieService.handleProductAddMovie(musicAddDTO, bindingResult, model);
    }

    // Delete a Movie
    @SlowExecutionWarning
    @GetMapping(MODERATOR_MOVIES_DELETE_URL + "{id}")
    public String deleteMovie(@PathVariable UUID id) {
        return movieService.deleteMovie(id);
    }
}
