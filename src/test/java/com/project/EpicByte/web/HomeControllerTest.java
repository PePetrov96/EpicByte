package com.project.EpicByte.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.INDEX_HTML;
import static com.project.EpicByte.util.Constants.INDEX_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private GreenMail greenMail;

    private UUID userID;
    private final MockHttpSession session = new MockHttpSession();

    @BeforeEach
    void setUp() {
        session.setAttribute("numItems", 1);

        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    @WithMockUser(username = "user")
    void index() throws Exception {
        mockMvc.perform(get(INDEX_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subscriberDTO"))
                .andExpect(view().name(INDEX_HTML));
    }

    @Test
    @WithMockUser(username = "user")
    void subscribe_fail_bindingResultHasErrors() throws Exception {
        mockMvc.perform(post("/subscribe")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("subscriberDTO"))
                .andExpect(view().name(INDEX_HTML));
    }

    @Test
    @WithMockUser(username = "user")
    void subscribe_success() throws Exception {
        mockMvc.perform(post("/subscribe")
                        .with(csrf())
                        .param("name", "TestName")
                        .param("email", "TestEmail"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + INDEX_URL));
    }

}