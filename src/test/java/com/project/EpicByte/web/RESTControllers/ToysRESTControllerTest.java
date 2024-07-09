package com.project.EpicByte.web.RESTControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.productRepositories.ToyRepository;
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
class ToysRESTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToyRepository toyRepository;

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

        if (this.toyRepository.count() == 0) {
            toyRepository.saveAndFlush(returnToy("Name One"));
            toyRepository.saveAndFlush(returnToy("Name Two"));
        }

        productID = returnToyId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        toyRepository.deleteAll();
    }

    @Test
    void getAllToys() throws Exception {
        List<Toy> toys = toyRepository.findAll();

        mockMvc.perform(get("/api/user/toys")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(toys.get(0).getProductName())))
                .andExpect(jsonPath("$[1].productName", is(toys.get(1).getProductName())));
    }

    @Test
    void getToysById() throws Exception {
        Toy toy = toyRepository.findToyById(productID);

        mockMvc.perform(get("/api/user/toys/" + toy.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName", is(toy.getProductName())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteToy() throws Exception {
        mockMvc.perform(delete("/api/admin/toys/" + productID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    private Toy returnToy(String name) {
        Toy toy = new Toy();

        toy.setDateCreated(LocalDate.now());
        toy.setNewProduct(true);
        toy.setProductType(ProductTypeEnum.BOOK);
        toy.setProductImageUrl("path/to/image.jpg");
        toy.setProductName(name);
        toy.setProductPrice(BigDecimal.valueOf(25.99));
        toy.setDescription("This is a book description.");
        toy.setBrand("Brand name");

        return toy;
    }

    private UUID returnToyId() {
        return this.toyRepository.findAll().get(0).getId();
    }
}