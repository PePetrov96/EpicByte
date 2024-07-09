package com.project.EpicByte.web.productControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.enums.MovieCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static com.project.EpicByte.util.Constants.PRODUCTS_ALL_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private GreenMail greenMail;

    private UUID uuid;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);

        if (userRepository.count() == 0) {
            userRepository.save(returnUser());
        }

        if (movieRepository.count() == 0) {
            movieRepository.save(returnMovie());
        }

        uuid = returnBookId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        userRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void viewMovieProductDetails() throws Exception {
        mockMvc.perform(get("/movie" + PRODUCT_DETAILS_URL + "/" + uuid)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("breadcrumbs"))
                .andExpect(model().attributeExists("productType"))
                .andExpect(model().attributeExists("productLinkType"))
                .andExpect(model().attributeExists("linkType"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("productDetails"))
                .andExpect(model().attributeExists("linkType"))
                .andExpect(view().name(PRODUCT_DETAILS_HTML));
    }

    @Test
    void displayMoviePage() throws Exception {
        mockMvc.perform(get(ALL_MOVIES_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("breadcrumbs"))
                .andExpect(model().attributeExists("productType"))
                .andExpect(model().attributeExists("productLinkType"))
                .andExpect(model().attributeExists("linkType"))
                .andExpect(model().attributeExists("selectedSortingOption"))
                .andExpect(model().attributeExists("productList"))
                .andExpect(view().name(PRODUCTS_ALL_HTML));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void displayProductAddMoviePage() throws Exception {
        mockMvc.perform(get(MODERATOR_PRODUCT_ADD_MOVIE_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("linkType"))
                .andExpect(model().attributeExists("productType"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("fieldsMap"))
                .andExpect(model().attributeExists("enumsList"))
                .andExpect(view().name(PRODUCT_ADD_HTML));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void handleProductAddMoviePage_fail_bindingResultHasErrors() throws Exception {
        mockMvc.perform(post(MODERATOR_PRODUCT_ADD_MOVIE_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("linkType"))
                .andExpect(model().attributeExists("productType"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("fieldsMap"))
                .andExpect(model().attributeExists("enumsList"))
                .andExpect(view().name(PRODUCT_ADD_HTML));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void deleteMovie() throws Exception {
        mockMvc.perform(get(MODERATOR_MOVIES_DELETE_URL + uuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + ALL_MOVIES_URL));
    }

    private UserEntity returnUser() {
        UserEntity admin = new UserEntity();
        admin.setUsername("user");
        admin.setPassword(passwordEncoder.encode("user"));
        admin.setEmail("test@softuni.bg");
        admin.setFirstName("Test");
        admin.setLastName("Test");
        return admin;
    }

    private Movie returnMovie() {
        Movie movie = new Movie();

        movie.setDateCreated(LocalDate.now());
        movie.setNewProduct(true);
        movie.setProductType(ProductTypeEnum.BOOK);
        movie.setProductImageUrl("path/to/image.jpg");
        movie.setProductName("New Book");
        movie.setProductPrice(BigDecimal.valueOf(25.99));
        movie.setDescription("This is a book description.");

        movie.setGenre("Genre");
        movie.setCarrier(MovieCarrierEnum.BluRay);

        return movie;
    }

    private UUID returnBookId() {
        return this.movieRepository.findAll().get(0).getId();
    }
}