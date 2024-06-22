package com.project.EpicByte.web.RESTControllers;

import com.project.EpicByte.model.dto.productDTOs.MusicAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.Music;
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
public class MusicRESTController {
    private final ProductRESTService productRESTService;

    @Autowired
    public MusicRESTController(ProductRESTService productRESTService) {
        this.productRESTService = productRESTService;
    }

    // ALL MUSIC View
    @GetMapping("/user/music")
    public ResponseEntity<List<? extends BaseProduct>> getAllMusic() {
        return ResponseEntity.ok(this.productRESTService.getAll("MUSIC"));
    }

    // SINGLE MUSIC View
    @GetMapping("/user/music/{id}")
    public ResponseEntity<? extends BaseProduct> getMusicById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productRESTService.getProduct(id, "MUSIC"));
    }

    // ADD MUSIC
    @PostMapping("/admin/music")
    public ResponseEntity<Music> addMusic(@Valid @RequestBody MusicAddDTO musicAddDTO) {
        Music savedMusic = productRESTService.saveMusic(musicAddDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMusic);
    }

    // DELETE MUSIC
    @DeleteMapping("/admin/music/{id}")
    public ResponseEntity<Music> deleteMusic(@PathVariable UUID id) {
        this.productRESTService.deleteProduct(id, "MUSIC");
        return ResponseEntity
                .noContent()
                .build();
    }
}
