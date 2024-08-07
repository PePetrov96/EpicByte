package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.productDTOs.MusicAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Music;
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
public class MusicRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public MusicRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    @ApiOperation(value = "Get all music", notes = "Returns a list of all the music")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the music list was empty",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/user/music")
    public ResponseEntity<List<? extends BaseProduct>> getAllMusic() {
        return ResponseEntity.ok(this.productRESTService.getAll("MUSIC"));
    }

    @ApiOperation(value = "Get a single music", notes = "Returns a music based on the UUID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the music was not found",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @SlowExecutionWarning
    @GetMapping("/user/music/{id}")
    public ResponseEntity<? extends BaseProduct> getMusicById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "MUSIC"));
    }

    @ApiOperation(value = "Add music", notes = "Add a new music to the database")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @SlowExecutionWarning
    @PostMapping("/admin/music")
    public ResponseEntity<Music> addMusic(@Valid @RequestBody MusicAddDTO musicAddDTO) {
        Music savedMusic = productRESTService.saveMusic(musicAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMusic);
    }

    @ApiOperation(value = "Delete music", notes = "Delete a music based on UUID")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @SlowExecutionWarning
    @DeleteMapping("/admin/music/{id}")
    public ResponseEntity<Music> deleteMusic(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "MUSIC");
        return ResponseEntity
                .noContent()
                .build();
    }
}
