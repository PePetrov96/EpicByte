package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Toy;
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
public class ToysRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public ToysRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    // ALL TOYS View
    @ApiOperation(value = "Get all toys", notes = "Returns a list of all the toys")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the toy list was empty",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/user/toys")
    public ResponseEntity<List<? extends BaseProduct>> getAllToys() {
        return ResponseEntity.ok(this.productRESTService.getAll("TOYS"));
    }

    // SINGLE TOYS View
    @ApiOperation(value = "Get a single toy", notes = "Returns a toy based on the UUID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the toy was not found",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/user/toys/{id}")
    public ResponseEntity<? extends BaseProduct> getToysById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "TOYS"));
    }

    @ApiOperation(value = "Add toy", notes = "Add a new toy to the database")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @SlowExecutionWarning
    @PostMapping("/admin/toys")
    public ResponseEntity<Toy> addToy(@Valid @RequestBody ToyAddDTO toyAddDTO) {
        Toy savedToy = productRESTService.saveToy(toyAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedToy);
    }

    @ApiOperation(value = "Delete toy", notes = "Delete a toy based on UUID")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @SlowExecutionWarning
    @DeleteMapping("/admin/toys/{id}")
    public ResponseEntity<Toy> deleteToy(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "TOYS");
        return ResponseEntity
                .noContent()
                .build();
    }
}
