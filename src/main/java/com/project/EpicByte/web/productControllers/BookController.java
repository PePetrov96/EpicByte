package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.service.productServices.BookService;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class BookController extends Breadcrumbs {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Display detailed single Book entity page
    @GetMapping("/book" + PRODUCT_DETAILS_URL + "/{id}")
    public String viewBookProductDetails(@PathVariable UUID id, Model model) {
        return this.bookService.displayDetailedViewBookPage(id, model);
    }

    // Display all Books page
    @GetMapping(ALL_BOOKS_URL)
    public String displayBooksPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.bookService.displayAllBooksPage(model, sort);
    }

    // Display Add Book page
    @GetMapping(MODERATOR_PRODUCT_ADD_BOOK_URL)
    public String displayProductAddBookPage(Model model) {
        return bookService.displayProductAddBookPage(model);
    }

    // Process Add new Book
    @PostMapping(MODERATOR_PRODUCT_ADD_BOOK_URL)
    public String handleProductAddBookPage(@Valid @ModelAttribute("product") BookAddDTO product, BindingResult bindingResult, Model model) {
        return bookService.handleProductAddBook(product, bindingResult, model);
    }
}
