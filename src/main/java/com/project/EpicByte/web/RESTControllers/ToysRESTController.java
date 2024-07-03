package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Toy;
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
public class ToysRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public ToysRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    // SINGLE TOYS View
    @GetMapping("/user/toys")
    public ResponseEntity<List<? extends BaseProduct>> getAllToys() {
        return ResponseEntity.ok(this.productRESTService.getAll("TOYS"));
    }

    // SINGLE TOYS View
    @GetMapping("/user/toys/{id}")
    public ResponseEntity<? extends BaseProduct> getToysById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "TOYS"));
    }

    // ADD TOY
    @PostMapping("/admin/toys")
    public ResponseEntity<Toy> addToy(@Valid @RequestBody ToyAddDTO toyAddDTO) {
        Toy savedToy = productRESTService.saveToy(toyAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedToy);
    }

    // DELETE TOY
    @DeleteMapping("/admin/toys/{id}")
    public ResponseEntity<Toy> deleteToy(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "TOYS");
        return ResponseEntity
                .noContent()
                .build();
    }
}
