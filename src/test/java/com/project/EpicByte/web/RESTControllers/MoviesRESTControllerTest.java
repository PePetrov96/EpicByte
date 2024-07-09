package com.project.EpicByte.web.RESTControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.enums.MovieCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.repository.productRepositories.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoviesRESTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

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

        if (this.movieRepository.count() == 0) {
            movieRepository.saveAndFlush(returnMovie("Name One"));
            movieRepository.saveAndFlush(returnMovie("Name Two"));
        }

        productID = returnMovieId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        movieRepository.deleteAll();
    }

    @Test
    void getAllMovies() throws Exception {
        List<Movie> movies = movieRepository.findAll();

        mockMvc.perform(get("/api/user/movies")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(movies.get(0).getProductName())))
                .andExpect(jsonPath("$[1].productName", is(movies.get(1).getProductName())));
    }

    @Test
    void getMoviesById() throws Exception {
        Movie movie = movieRepository.findMovieById(productID);

        mockMvc.perform(get("/api/user/movies/" + movie.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName", is(movie.getProductName())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteMovie() throws Exception {
        mockMvc.perform(delete("/api/admin/movies/" + productID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    private Movie returnMovie(String name) {
        Movie movie = new Movie();

        movie.setDateCreated(LocalDate.now());
        movie.setNewProduct(true);
        movie.setProductType(ProductTypeEnum.BOOK);
        movie.setProductImageUrl("path/to/image.jpg");
        movie.setProductName(name);
        movie.setProductPrice(BigDecimal.valueOf(25.99));
        movie.setDescription("This is a book description.");
        movie.setGenre("Genre");
        movie.setCarrier(MovieCarrierEnum.BluRay);

        return movie;
    }

    private UUID returnMovieId() {
        return this.movieRepository.findAll().get(0).getId();
    }
}