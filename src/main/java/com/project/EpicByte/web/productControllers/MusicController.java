package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.MusicAddDTO;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.service.MusicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.project.EpicByte.util.Constants.ADMIN_PRODUCT_ADD_MUSIC_URL;
import static com.project.EpicByte.util.Constants.PRODUCT_DETAILS_URL;

@Controller
public class MusicController {
    private final MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    // Display detailed single Music entity page
    @GetMapping(PRODUCT_DETAILS_URL + "/music/" + "{id}")
    public String viewMusicProductDetails(@PathVariable Long id, Model model) {
        return musicService.displayDetailedViewMusicPage(id, model);
    }

    // Display all Music page
    @GetMapping("/products/music")
    public String displayMusicPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.musicService.displayAllMusicPage(model, sort);
    }

    // Display Add Music page
    @GetMapping(ADMIN_PRODUCT_ADD_MUSIC_URL)
    protected String displayProductAddMusicPage(Model model) {
        return musicService.displayProductAddMusicPage(model);
    }

    // Process Add new Music
    @PostMapping(ADMIN_PRODUCT_ADD_MUSIC_URL)
    public String handleProductAddMusicPage(@Valid @ModelAttribute("product") MusicAddDTO musicAddDTO,
                                            BindingResult bindingResult, Model model) {
        return musicService.handleProductAddMusic(musicAddDTO, bindingResult, model);
    }
}
