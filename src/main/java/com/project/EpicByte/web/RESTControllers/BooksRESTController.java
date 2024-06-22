package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Book;
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
public class BooksRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public BooksRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    // ALL BOOKS View
    @GetMapping("/user/books")
    public ResponseEntity<List<? extends BaseProduct>> getAllBooks() {
        return ResponseEntity.ok(this.productRESTService.getAll("BOOKS"));
    }

    // SINGLE BOOK View
    @GetMapping("/user/books/{id}")
    public ResponseEntity<? extends BaseProduct> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "BOOKS"));
    }

    // ADD BOOK
    @PostMapping("/admin/books")
    public ResponseEntity<Book> addBook(@Valid @RequestBody BookAddDTO bookAddDTO) {
        Book savedBook = productRESTService.saveBook(bookAddDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    // DELETE BOOK
    @DeleteMapping("/admin/books/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "BOOKS");
        return ResponseEntity
                .noContent()
                .build();
    }
}

