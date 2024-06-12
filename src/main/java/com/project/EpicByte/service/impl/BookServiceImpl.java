package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.BookAddDTO;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.repository.BookRepository;
import com.project.EpicByte.service.BookService;
import com.project.EpicByte.util.Breadcrumbs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.project.EpicByte.util.Constants.*;

@Service
public class BookServiceImpl extends Breadcrumbs implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }


    @Override
    public String displayProductAddBookPage(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("book.text", null, locale);

        model.addAttribute("linkType", "book");
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("product", new BookAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("book"));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddBook(BookAddDTO bookAddDTO, BindingResult bindingResult, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("book.text", null, locale);

        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "book");

        if (bindingResult.hasErrors()) {
            model.addAttribute("linkType", "book");
            model.addAttribute("fieldsMap", getFieldNames("book"));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addBookToDatabase(bookAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Book added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllBooksPage(Model model, String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("books.text", null, locale);

//        addProductBreadcrumb(model, null, productTypeMessage);
        addProductBreadcrumb(model, null, "Books");
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "books");

        List<Book> bookList;

        if (sort == null) {
            bookList = this.bookRepository.findAll();
        } else if (sort.equals("lowest")) {
            bookList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            bookList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            bookList = getAllSortedAlphabetically();
        } else {
            bookList = this.bookRepository.findAll();
        }

        model.addAttribute("productList", bookList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewBookPage(Long id, Model model) {
        Book book = bookRepository.findBookById(id);

        addProductBreadcrumb(model, null,  "Books", book.getProductName());
        model.addAttribute("product", book);
        model.addAttribute("productDetails", null);

        return "product-details";
    }


    // Support methods
    private void addBookToDatabase(BookAddDTO bookAddDTO) {
        Book book = modelMapper.map(bookAddDTO, Book.class);

        book.setNewProduct(true);
        book.setDateCreated(LocalDate.now());
        book.setProductType(ProductTypeEnum.BOOK);

        bookRepository.saveAndFlush(book);
    }

    private List<Book> getAllSortedByLowestPrice() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Book> getAllSortedByHighestPrice() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Book> getAllSortedAlphabetically() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }
}
