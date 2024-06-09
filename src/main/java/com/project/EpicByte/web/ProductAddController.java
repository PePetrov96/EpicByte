package com.project.EpicByte.web;

import com.project.EpicByte.model.dto.*;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.service.*;
import com.project.EpicByte.util.FieldNamesGenerator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class ProductAddController extends FieldNamesGenerator {
    private final BookService bookService;
    private final TextbookService textbookService;
    private final MusicService musicService;
    private final MovieService movieService;
    private final ToyService toyService;

    public ProductAddController(BookService bookService, TextbookService textbookService, MusicService musicService, MovieService movieService, ToyService toyService) {
        this.bookService = bookService;
        this.textbookService = textbookService;
        this.musicService = musicService;
        this.movieService = movieService;
        this.toyService = toyService;
    }

    // Add BOOK handle
    @GetMapping(ADMIN_PRODUCT_ADD_BOOK_URL)
    public String displayProductAddBookPage(Model model) {
        return bookService.displayProductAddBookPage(model);
    }

    @PostMapping(ADMIN_PRODUCT_ADD_BOOK_URL)
    public String handleProductAddBookPage(@Valid @ModelAttribute("product") BookAddDTO product, BindingResult bindingResult, Model model) {
        return bookService.handleProductAddBook(product, bindingResult, model);
    }

    // Add TEXTBOOK handle
    @GetMapping(ADMIN_PRODUCT_ADD_TEXTBOOK_URL)
    public String displayProductAddTextbookPage(Model model) {
        return textbookService.displayProductAddTextbookPage(model);
    }

    @PostMapping(ADMIN_PRODUCT_ADD_TEXTBOOK_URL)
    public String handleProductAddTextbookPage(@Valid @ModelAttribute("product") TextbookAddDTO textbookAddDTODTO,
                                               BindingResult bindingResult, Model model) {
        return textbookService.handleProductAddTextbook(textbookAddDTODTO, bindingResult, model);
    }

    // Add MUSIC handle
    @GetMapping(ADMIN_PRODUCT_ADD_MUSIC_URL)
    protected String displayProductAddMusicPage(Model model) {
        return musicService.displayProductAddMusicPage(model);
    }
    @PostMapping(ADMIN_PRODUCT_ADD_MUSIC_URL)
    public String handleProductAddMusicPage(@Valid @ModelAttribute("product") MusicAddDTO musicAddDTO, BindingResult bindingResult, Model model) {
        return musicService.handleProductAddMusic(musicAddDTO, bindingResult, model);
    }

    // Add MOVIE handle
    @GetMapping(ADMIN_PRODUCT_ADD_MOVIE_URL)
    protected String displayProductAddMoviePage(Model model) {
        return movieService.displayProductAddMoviePage(model);
    }
    @PostMapping(ADMIN_PRODUCT_ADD_MOVIE_URL)
    public String handleProductAddMoviePage(@Valid @ModelAttribute("product")MovieAddDTO musicAddDTO, BindingResult bindingResult, Model model) {
        return movieService.handleProductAddMovie(musicAddDTO, bindingResult, model);
    }

    // Add TOY handle
    @GetMapping(ADMIN_PRODUCT_ADD_TOY_URL)
    protected String displayProductAddToyPage(Model model) {
        return toyService.displayProductAddToyPage(model);
    }
    @PostMapping(ADMIN_PRODUCT_ADD_TOY_URL)
    public String handleProductAddToyPage(@Valid @ModelAttribute("product") ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        return toyService.handleProductAddToy(toyAddDTO, bindingResult, model);
    }
}
