package com.project.EpicByte.web.RESTControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.repository.productRepositories.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BooksRESTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private GreenMail greenMail;

    private UUID productID;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);

        if (this.bookRepository.count() == 0) {
            bookRepository.saveAndFlush(returnBook("Name One"));
            bookRepository.saveAndFlush(returnBook("Name Two"));
        }

        productID = returnBookId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        bookRepository.deleteAll();
    }

    @Test
    void getAllBooks() throws Exception {
        List<Book> books = bookRepository.findAll();

        mockMvc.perform(get("/api/user/books")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(books.get(0).getProductName())))
                .andExpect(jsonPath("$[1].productName", is(books.get(1).getProductName())));
    }

    @Test
    void getBookById() throws Exception {
        Book book = bookRepository.findBookById(productID);

        mockMvc.perform(get("/api/user/books/" + book.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName", is(book.getProductName())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/api/admin/books/" + productID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    private Book returnBook(String name) {
        Book book = new Book();

        book.setDateCreated(LocalDate.now());
        book.setNewProduct(true);
        book.setProductType(ProductTypeEnum.BOOK);
        book.setProductImageUrl("path/to/image.jpg");
        book.setProductName(name);
        book.setProductPrice(BigDecimal.valueOf(25.99));
        book.setDescription("This is a book description.");
        book.setAuthorName("John Doe");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2022, 1, 1));
        book.setLanguage(LanguageEnum.English);
        book.setPrintLength(300);
        book.setDimensions("20 x 15 x 3");

        return book;
    }

    private UUID returnBookId() {
        return this.bookRepository.findAll().get(0).getId();
    }
}