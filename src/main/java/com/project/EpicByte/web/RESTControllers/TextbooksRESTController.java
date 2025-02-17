//package com.project.EpicByte.web.RESTControllers;
//
//import com.project.EpicByte.aop.SlowExecutionWarning;
//import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
//import com.project.EpicByte.model.entity.BaseProduct;
//import com.project.EpicByte.model.entity.productEntities.Textbook;
//import com.project.EpicByte.service.ProductRESTService;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api")
//public class TextbooksRESTController {
//    private final ProductRESTService productRESTService;
//
//    @Autowired
//    public TextbooksRESTController(ProductRESTService productRESTService) {
//        this.productRESTService = productRESTService;
//    }
//
//    @ApiOperation(value = "Get all textbooks", notes = "Returns a list of all the textbooks")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "If the textbook list was empty",
//                    content = {
//                            @Content(
//                                    mediaType = "application/json"
//                            )
//                    }
//            )
//    })
//    @SlowExecutionWarning
//    @GetMapping("/user/textbooks")
//    public ResponseEntity<List<? extends BaseProduct>> getAllTextbooks() {
//        return ResponseEntity.ok(this.productRESTService.getAll("TEXTBOOKS"));
//    }
//
//    @ApiOperation(value = "Get a single textbook", notes = "Returns a textbook based on the UUID")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "If the textbook was not found",
//                    content = {
//                            @Content(
//                                    mediaType = "application/json"
//                            )
//                    }
//            )
//    })
//    @SlowExecutionWarning
//    @GetMapping("/user/textbooks/{id}")
//    public ResponseEntity<? extends BaseProduct> getTextbookById(@PathVariable UUID id) {
//        return ResponseEntity.ok(this.productRESTService.getProduct(id, "TEXTBOOKS"));
//    }
//
//    @ApiOperation(value = "Add textbook", notes = "Add a new textbook to the database")
//    @Operation(
//            security = @SecurityRequirement(
//                    name = "bearer-token"
//            )
//    )
//    @SlowExecutionWarning
//    @PostMapping("/admin/textbooks")
//    public ResponseEntity<Textbook> addTextbook(@Valid @RequestBody TextbookAddDTO textbookAddDTO) {
//        Textbook savedTextbook = productRESTService.saveTextbook(textbookAddDTO);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(savedTextbook);
//    }
//
//    @ApiOperation(value = "Delete textbook", notes = "Delete a textbook based on UUID")
//    @Operation(
//            security = @SecurityRequirement(
//                    name = "bearer-token"
//            )
//    )
//    @SlowExecutionWarning
//    @DeleteMapping("/admin/textbooks/{id}")
//    public ResponseEntity<Textbook> deleteTextbook(@PathVariable UUID id) {
//        this.productRESTService.deleteProduct(id, "TEXTBOOKS");
//        return ResponseEntity
//                .noContent()
//                .build();
//    }
//}
