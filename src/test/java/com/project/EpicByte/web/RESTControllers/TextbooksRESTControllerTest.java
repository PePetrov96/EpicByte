package com.project.EpicByte.web.RESTControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.repository.productRepositories.TextbookRepository;
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
class TextbooksRESTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TextbookRepository textbookRepository;

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

        if (this.textbookRepository.count() == 0) {
            textbookRepository.saveAndFlush(returnTextbook("Name One"));
            textbookRepository.saveAndFlush(returnTextbook("Name Two"));
        }

        productID = returnTextbookId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        textbookRepository.deleteAll();
    }

    @Test
    void getAllTextbooks() throws Exception {
        List<Textbook> textbooks = textbookRepository.findAll();

        mockMvc.perform(get("/api/user/textbooks")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(textbooks.get(0).getProductName())))
                .andExpect(jsonPath("$[1].productName", is(textbooks.get(1).getProductName())));
    }

    @Test
    void getTextbookById() throws Exception {
        Textbook textbook = textbookRepository.findTextbookById(productID);

        mockMvc.perform(get("/api/user/textbooks/" + textbook.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName", is(textbook.getProductName())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteTextbook() throws Exception {
        mockMvc.perform(delete("/api/admin/textbooks/" + productID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    private Textbook returnTextbook(String name) {
        Textbook textbook = new Textbook();

        textbook.setDateCreated(LocalDate.now());
        textbook.setNewProduct(true);
        textbook.setProductType(ProductTypeEnum.TEXTBOOK);
        textbook.setProductImageUrl("path/to/image.jpg");
        textbook.setProductName(name);
        textbook.setProductPrice(BigDecimal.valueOf(25.99));
        textbook.setDescription("This is a textbook description.");
        textbook.setAuthorName("John Doe");
        textbook.setPublisher("Pearson");
        textbook.setPublicationDate(LocalDate.of(2022, 1, 1));
        textbook.setLanguage(LanguageEnum.English);
        textbook.setPrintLength(300);
        textbook.setDimensions("20 x 15 x 3");

        return textbook;
    }

    private UUID returnTextbookId() {
        return this.textbookRepository.findAll().get(0).getId();
    }

}