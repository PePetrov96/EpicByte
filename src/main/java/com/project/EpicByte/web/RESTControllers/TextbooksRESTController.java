package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.service.REST.ProductRESTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TextbooksRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public TextbooksRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    // ALL TEXTBOOKS View
    @GetMapping("/user/textbooks")
    public ResponseEntity<List<? extends BaseProduct>> getAllTextbooks() {
        return ResponseEntity.ok(this.productRESTService.getAll("TEXTBOOKS"));
    }

    // SINGLE TEXTBOOK View
    @GetMapping("/user/textbooks/{id}")
    public ResponseEntity<? extends BaseProduct> getTextbookById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "TEXTBOOKS"));
    }

    // ADD TEXTBOOK
    @PostMapping("/admin/textbooks")
    public ResponseEntity<Textbook> addTextbook(@Valid @RequestBody TextbookAddDTO textbookAddDTO) {
        Textbook savedTextbook = productRESTService.saveTextbook(textbookAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTextbook);
    }

    // DELETE TEXTBOOK
    @DeleteMapping("/admin/textbooks/{id}")
    public ResponseEntity<Textbook> deleteTextbook(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "TEXTBOOKS");
        return ResponseEntity
                .noContent()
                .build();
    }
}
