package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.service.ProductRESTService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @ApiOperation(value = "Get all movies", notes = "Returns a list of all the movies")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the movies list was empty",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/user/movies")
    public ResponseEntity<List<? extends BaseProduct>> getAllMovies() {
        return ResponseEntity.ok(this.productRESTService.getAll("MOVIES"));
    }

    @ApiOperation(value = "Get a single movie", notes = "Returns a movie based on the UUID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the movie was not found",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/user/movies/{id}")
    public ResponseEntity<? extends BaseProduct> getMoviesById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "MOVIES"));
    }

    @ApiOperation(value = "Add movie", notes = "Add a new movie to the database")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @SlowExecutionWarning
    @PostMapping("/admin/movies")
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieAddDTO movieAddDTO) {
        Movie savedMovie = productRESTService.saveMovie(movieAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMovie);
    }

    @ApiOperation(value = "Delete movie", notes = "Delete a movie based on UUID")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @SlowExecutionWarning
    @DeleteMapping("/admin/movies/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "MOVIES");
        return ResponseEntity
                .noContent()
                .build();
    }
}
