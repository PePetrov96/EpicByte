package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Display detailed single Book entity page
    @SlowExecutionWarning
    @GetMapping("/book" + PRODUCT_DETAILS_URL + "/{id}")
    public String viewBookProductDetails(@PathVariable UUID id, Model model) {
        return this.bookService.displayDetailedViewBookPage(id, model); // return PRODUCT_DETAILS_HTML;
    }

    // Display all Books page
    @SlowExecutionWarning
    @GetMapping(ALL_BOOKS_URL)
    public String displayBooksPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.bookService.displayAllBooksPage(model, sort); // return PRODUCTS_ALL_HTML;
    }

    // Display Add Book page
    @SlowExecutionWarning
    @GetMapping(MODERATOR_PRODUCT_ADD_BOOK_URL)
    public String displayProductAddBookPage(Model model) {
        return bookService.displayProductAddBookPage(model); // return PRODUCT_ADD_HTML;
    }

    // Process Add new Book
    @SlowExecutionWarning
    @PostMapping(MODERATOR_PRODUCT_ADD_BOOK_URL)
    public String handleProductAddBookPage(@Valid @ModelAttribute("product") BookAddDTO product, BindingResult bindingResult, Model model) {
        return bookService.handleProductAddBook(product, bindingResult, model); // return DISPLAY_TEXT_HTML;
    }

    // Delete a book
    @SlowExecutionWarning
    @GetMapping(MODERATOR_BOOKS_DELETE_URL + "{id}")
    public String deleteBook(@PathVariable UUID id) {
        return bookService.deleteBook(id); // return "redirect:" + ALL_BOOKS_URL;
    }
}
