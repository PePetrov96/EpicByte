package com.project.EpicByte.web.productControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.ToyRepository;
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
class ToyControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToyRepository toyRepository;

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

        if (toyRepository.count() == 0) {
            toyRepository.save(returnBook());
        }

        uuid = returnBookId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        userRepository.deleteAll();
        toyRepository.deleteAll();
    }

    @Test
    void viewToyProductDetails() throws Exception {
        mockMvc.perform(get("/toy" + PRODUCT_DETAILS_URL + "/" + uuid)
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
    void displayToysPage() throws Exception {
        mockMvc.perform(get(ALL_TOYS_URL)
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
    void displayProductAddToyPage() throws Exception {
        mockMvc.perform(get(MODERATOR_PRODUCT_ADD_TOY_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("linkType"))
                .andExpect(model().attributeExists("productType"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("fieldsMap"))
                .andExpect(view().name(PRODUCT_ADD_HTML));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void handleProductAddToyPage() throws Exception {
        mockMvc.perform(post(MODERATOR_PRODUCT_ADD_TOY_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("linkType"))
                .andExpect(model().attributeExists("productType"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("fieldsMap"))
                .andExpect(view().name(PRODUCT_ADD_HTML));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    void deleteToy() throws Exception {
        mockMvc.perform(get(MODERATOR_TOYS_DELETE_URL + uuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + ALL_TOYS_URL));
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

    private Toy returnBook() {
        Toy toy = new Toy();

        toy.setDateCreated(LocalDate.now());
        toy.setNewProduct(true);
        toy.setProductType(ProductTypeEnum.BOOK);
        toy.setProductImageUrl("path/to/image.jpg");
        toy.setProductName("New Book");
        toy.setProductPrice(BigDecimal.valueOf(25.99));
        toy.setDescription("This is a toy description.");

        toy.setBrand("Brand");

        return toy;
    }

    private UUID returnBookId() {
        return this.toyRepository.findAll().get(0).getId();
    }
}