package com.project.EpicByte.service.impl.productServices;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.repository.BookRepository;
import com.project.EpicByte.service.productServices.BookService;
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
import java.util.*;

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
        model.addAttribute("linkType", "book");
        model.addAttribute("productType", getLocalizedText("book.text"));
        model.addAttribute("product", new BookAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("book", false));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddBook(BookAddDTO bookAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", getLocalizedText("book.text"));
        model.addAttribute("linkType", "book");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("book", false));
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
        addProductBreadcrumb(model, ALL_BOOKS_URL, "Books");
        model.addAttribute("productType", getLocalizedText("books.text"));
        model.addAttribute("productLinkType", "book");
        model.addAttribute("linkType", "books");

        List<Book> bookList;

        if (sort == null) {
            bookList = getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            bookList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            bookList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            bookList = getAllSortedAlphabetically();
        } else {
            bookList = getAllSortedByIsNewProduct();
        }

        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));

        model.addAttribute("productList", bookList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewBookPage(UUID id, Model model) {
        Book book = bookRepository.findBookById(id);

        addProductBreadcrumb(model, ALL_BOOKS_URL, "Books", book.getProductName());
        model.addAttribute("product", book);
        model.addAttribute("productDetails", getDetailFields(book));

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
    private Map<String, String> getDetailFields(Book book) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("authorName.text"), book.getAuthorName());
        fieldsMap.put(getLocalizedText("publisher.text"), book.getPublisher());
        fieldsMap.put(getLocalizedText("publicationDate.text"), book.getPublicationDate().toString());
        fieldsMap.put(getLocalizedText("language.text"), book.getLanguage().toString());
        fieldsMap.put(getLocalizedText("printLength.text"), book.getPrintLength().toString() + " pages");
        fieldsMap.put(getLocalizedText("dimensions.text"), book.getDimensions());

        return fieldsMap;
    }

    private void addBookToDatabase(BookAddDTO bookAddDTO) {
        Book book = modelMapper.map(bookAddDTO, Book.class);

        book.setNewProduct(true);
        book.setDateCreated(LocalDate.now());
        book.setProductType(ProductTypeEnum.BOOK);

        bookRepository.saveAndFlush(book);
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private List<Book> getAllSortedByIsNewProduct() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC,"isNewProduct"));
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
