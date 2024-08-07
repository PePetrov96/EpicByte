package com.project.EpicByte.service.impl.productImpl;

import com.project.EpicByte.exceptions.NoSuchProductException;
import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.BookRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.BookService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
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
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final Breadcrumbs breadcrumbs;
    private final FieldNamesGenerator fieldNamesGenerator;
    // CLOUDINARY
    private final ProductImagesService productImagesService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           CartRepository cartRepository,
                           ModelMapper modelMapper,
                           MessageSource messageSource,
                           Breadcrumbs breadcrumbs,
                           FieldNamesGenerator fieldNamesGenerator,
                           ProductImagesService productImagesService) {
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.breadcrumbs = breadcrumbs;
        this.fieldNamesGenerator = fieldNamesGenerator;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddBookPage(Model model) {
        addDefaultModelAttributesForAddAndHandle(model);
        model.addAttribute("product", new BookAddDTO());
        model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("book", false));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddBook(BookAddDTO bookAddDTO, BindingResult bindingResult, Model model) {
        addDefaultModelAttributesForAddAndHandle(model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("book", false));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addBookToDatabase(bookAddDTO, model);
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllBooksPage(Model model, String sort) {
        breadcrumbs.addProductBreadcrumb(model, ALL_BOOKS_URL, "Books");
        addDefaultModelAttributesForAllAndDetailed(model);
        List<Book> bookList = getSortedBooks(sort);
        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));
        model.addAttribute("productList", bookList);
        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewBookPage(UUID id, Model model) {
        Book book = getBookOrThrowException(id);
        breadcrumbs.addProductBreadcrumb(model, ALL_BOOKS_URL, "Books", book.getProductName());
        addDefaultModelAttributesForAllAndDetailed(model);
        model.addAttribute("product", book);
        model.addAttribute("productDetails", getDetailFields(book));
        model.addAttribute("linkType", "books");
        return PRODUCT_DETAILS_HTML;
    }

    @Override
    public String deleteBook(UUID id) {
        deleteBookFromDatabase(id);
        return "redirect:" + ALL_BOOKS_URL;
    }

    // Support methods
    private Book getBookOrThrowException(UUID id){
        Book book = bookRepository.findBookById(id);
        if (book == null) throw new NoSuchProductException();
        return book;
    }

    private void addDefaultModelAttributesForAddAndHandle(Model model) {
        model.addAttribute("linkType", "book");
        model.addAttribute("productType", getLocalizedText("book.text"));
    }

    private void addDefaultModelAttributesForAllAndDetailed(Model model) {
        model.addAttribute("productType", getLocalizedText("books.text"));
        model.addAttribute("productLinkType", "book");
        model.addAttribute("linkType", "books");
    }

    private List<Book> getSortedBooks(String sort) {
        if (sort == null || sort.equals("default")) {
            return getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            return getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            return getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            return getAllSortedAlphabetically();
        } else {
            return getAllSortedByIsNewProduct();
        }
    }

    private void deleteBookFromDatabase(UUID id) {
        Book book = bookRepository.findBookById(id);

        // Remove the image from Cloudinary
        this.productImagesService.removeImageURL(book.getProductImageUrl());

        // Remove the image from the repository
        this.bookRepository.delete(book);

        // Remove the product from all user carts
        List<CartItem> cartItemList = this.cartRepository.findAllByProductId(id);
        this.cartRepository.deleteAll(cartItemList);
    }

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

    private void addBookToDatabase(BookAddDTO bookAddDTO, Model model) {
        Book book = modelMapper.map(bookAddDTO, Book.class);
        setNewBookDetails(book, bookAddDTO);
        bookRepository.saveAndFlush(book);
        setSuccessMessageToModel(model);
    }

    private void setNewBookDetails(Book book, BookAddDTO bookAddDTO) {
        book.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                bookAddDTO.getProductImageUrl()));
        book.setNewProduct(true);
        book.setDateCreated(LocalDate.now());
        book.setProductType(ProductTypeEnum.BOOK);
    }

    private void setSuccessMessageToModel(Model model) {
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("book.added.successfully.text"));
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
