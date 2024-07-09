package com.project.EpicByte.web.productControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.MusicRepository;
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
class MusicControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

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

        if (musicRepository.count() == 0) {
            musicRepository.save(returnMusic());
        }

        uuid = returnBookId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        userRepository.deleteAll();
        musicRepository.deleteAll();
    }

    @Test
    void viewMusicProductDetails() throws Exception {
        mockMvc.perform(get("/music" + PRODUCT_DETAILS_URL + "/" + uuid)
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
    void displayMusicPage() throws Exception {
        mockMvc.perform(get(ALL_MUSIC_URL)
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
    void displayProductAddMusicPage() throws Exception {
        mockMvc.perform(get(MODERATOR_PRODUCT_ADD_MUSIC_URL)
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
    void handleProductAddMusicPage() throws Exception {
        mockMvc.perform(post(MODERATOR_PRODUCT_ADD_MUSIC_URL)
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
    void deleteMusic() throws Exception {
        mockMvc.perform(get(MODERATOR_MUSIC_DELETE_URL + uuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + ALL_MUSIC_URL));
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

    private Music returnMusic() {
        Music book = new Music();

        book.setDateCreated(LocalDate.now());
        book.setNewProduct(true);
        book.setProductType(ProductTypeEnum.BOOK);
        book.setProductImageUrl("path/to/image.jpg");
        book.setProductName("New Book");
        book.setProductPrice(BigDecimal.valueOf(25.99));
        book.setDescription("This is a book description.");

        book.setArtistName("John Doe");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2022, 1, 1));
        book.setCarrier(MusicCarrierEnum.Vinyl);
        book.setGenre("Genre");

        return book;
    }

    private UUID returnBookId() {
        return this.musicRepository.findAll().get(0).getId();
    }
}