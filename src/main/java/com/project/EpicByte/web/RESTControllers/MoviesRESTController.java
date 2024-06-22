package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.service.ProductRESTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MoviesRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public MoviesRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    // SINGLE MOVIES View
    @GetMapping("/user/movies")
    public ResponseEntity<List<? extends BaseProduct>> getAllMovies() {
        return ResponseEntity.ok(this.productRESTService.getAll("MOVIES"));
    }

    // SINGLE MOVIES View
    @GetMapping("/user/movies/{id}")
    public ResponseEntity<? extends BaseProduct> getMoviesById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "MOVIES"));
    }

    // ADD MOVIE
    @PostMapping("/admin/movies")
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieAddDTO movieAddDTO) {
        Movie savedMovie = productRESTService.saveMovie(movieAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMovie);
    }

    // DELETE MOVIE
    @DeleteMapping("/admin/movies/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "MOVIES");
        return ResponseEntity
                .noContent()
                .build();
    }
}
