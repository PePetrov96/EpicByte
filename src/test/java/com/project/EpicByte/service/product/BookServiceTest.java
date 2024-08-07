package com.project.EpicByte.service.product;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.BookRepository;
import com.project.EpicByte.service.impl.productImpl.BookServiceImpl;
import com.project.EpicByte.service.BookService;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductImagesService productImagesService;

    @Mock
    private Breadcrumbs breadcrumbs;

    @Mock
    private Model model;

    private BindingResult bindingResult;
    private BookService bookService;
    private BookAddDTO bookAddDTO;
    private Book book;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        FieldNamesGenerator fieldNamesGenerator = new FieldNamesGenerator(messageSource);
        this.breadcrumbs = new Breadcrumbs(messageSource);

        this.bookService = new BookServiceImpl(bookRepository,
                cartRepository,
                modelMapper,
                messageSource,
                breadcrumbs,
                fieldNamesGenerator,
                productImagesService);

        this.bindingResult = mock(BindingResult.class);

        this.bookAddDTO = new BookAddDTO();
        bookAddDTO.setProductImageUrl("testURK");
        bookAddDTO.setProductName("testName");
        bookAddDTO.setProductPrice(10.00);
        bookAddDTO.setDescription("testDescription");
        bookAddDTO.setAuthorName("testAuthor");
        bookAddDTO.setPublisher("testPublisher");
        bookAddDTO.setPublicationDate(LocalDate.now().minusYears(1));
        bookAddDTO.setLanguage("English");
        bookAddDTO.setPrintLength(100);
        bookAddDTO.setDimensions("testDimensions");

        this.book = modelMapper.map(bookAddDTO, Book.class);
    }

    @Test
    public void displayProductAddBookPage_success() {
        String actualPage = this.bookService.displayProductAddBookPage(model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void handleProductAddBook_success() {
        String actualPage = this.bookService.handleProductAddBook(bookAddDTO, bindingResult, model);
        Assertions.assertEquals(DISPLAY_TEXT_HTML, actualPage);
    }

    @Test
    public void handleProductAddBook_fail_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String actualPage = this.bookService.handleProductAddBook(new BookAddDTO(), bindingResult, model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void displayAllBooksPage_success() {
        String actualPage = this.bookService.displayAllBooksPage(this.model, "default");
        Assertions.assertEquals(PRODUCTS_ALL_HTML, actualPage);
    }

    @Test
    public void displayDetailedViewBookPage_success() {
        when(bookRepository.findBookById(any())).thenReturn(book);
        String actualPage = this.bookService.displayDetailedViewBookPage(UUID.randomUUID(), model);
        Assertions.assertEquals(PRODUCT_DETAILS_HTML, actualPage);
    }

    @Test
    public void deleteBook_success() {
        when(bookRepository.findBookById(any())).thenReturn(book);
        when(productImagesService.removeImageURL(any())).thenReturn(true);
        String actualPage = this.bookService.deleteBook(UUID.randomUUID());
        Assertions.assertEquals("redirect:" + ALL_BOOKS_URL, actualPage);
    }
}
