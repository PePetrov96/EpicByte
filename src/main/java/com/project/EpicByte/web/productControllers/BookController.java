package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.BookAddDTO;
import com.project.EpicByte.service.BookService;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.project.EpicByte.util.Constants.ADMIN_PRODUCT_ADD_BOOK_URL;
import static com.project.EpicByte.util.Constants.PRODUCT_DETAILS_URL;

@Controller
public class BookController extends Breadcrumbs {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Display detailed single Book entity page
    @GetMapping(PRODUCT_DETAILS_URL + "/books/" + "{id}")
    public String viewBookProductDetails(@PathVariable Long id, Model model) {
        return this.bookService.displayDetailedViewBookPage(id, model);
    }

    // Display all Books page
    @GetMapping("/products/books")
    public String displayBooksPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.bookService.displayAllBooksPage(model, sort);
    }

    // Display Add Book page
    @GetMapping(ADMIN_PRODUCT_ADD_BOOK_URL)
    public String displayProductAddBookPage(Model model) {
        return bookService.displayProductAddBookPage(model);
    }

    // Process Add new Book
    @PostMapping(ADMIN_PRODUCT_ADD_BOOK_URL)
    public String handleProductAddBookPage(@Valid @ModelAttribute("product") BookAddDTO product, BindingResult bindingResult, Model model) {
        return bookService.handleProductAddBook(product, bindingResult, model);
    }
}
