package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.service.RESTService.ProductRESTService;
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
public class BooksRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public BooksRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    @ApiOperation(value = "Get all books", notes = "Returns a list of all the books")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the books list was empty",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @GetMapping("/user/books")
    public ResponseEntity<List<? extends BaseProduct>> getAllBooks() {
        return ResponseEntity.ok(this.productRESTService.getAll("BOOKS"));
    }

    @ApiOperation(value = "Get a single book", notes = "Returns a book based on the UUID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "If the book was not found",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @GetMapping("/user/books/{id}")
    public ResponseEntity<? extends BaseProduct> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "BOOKS"));
    }

    @ApiOperation(value = "Add book", notes = "Add a new book to the database")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @PostMapping("/admin/books")
    public ResponseEntity<Book> addBook(@Valid @RequestBody BookAddDTO bookAddDTO) {
        Book savedBook = productRESTService.saveBook(bookAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedBook);
    }

    @ApiOperation(value = "Delete book", notes = "Delete a book based on UUID")
    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @DeleteMapping("/admin/books/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "BOOKS");
        return ResponseEntity
                .noContent()
                .build();
    }
}

