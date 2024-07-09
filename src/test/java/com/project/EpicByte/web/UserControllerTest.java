package com.project.EpicByte.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserRepository;
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


import static com.project.EpicByte.util.Constants.AUTH_LOGIN_HTML;
import static com.project.EpicByte.util.Constants.AUTH_REGISTER_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    public void login() throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(returnUser());
        }

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "admin")
                        .param("password", "admin"))
                .andExpect(status().isFound());
    }

    @Test
    public void showLoginPage() throws Exception {
        mockMvc.perform(get("/login")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(AUTH_LOGIN_HTML));
    }

    @Test
    public void handleLoginError() throws Exception {
        mockMvc.perform(post("/login-error")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loginError"))
                .andExpect(view().name(AUTH_LOGIN_HTML));
    }

    @Test
    public void showRegisterPage() throws Exception {
        mockMvc.perform(get("/register")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userRegisterDTO"))
                .andExpect(view().name(AUTH_REGISTER_HTML));
    }

    @Test
    public void registerUser() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("firstName", "TestFirstName")
                        .param("lastName", "TestLastName")
                        .param("username", "TestUsername")
                        .param("email", "testemail@email.com")
                        .param("password", "123")
                        .param("repeatPassword", "123")
                        .param("termsAndConditionsAgreed", String.valueOf(true))
                ).andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "user")
    public void showProfilePage() throws Exception {
        mockMvc.perform(get("/user/profile")
                        .with(csrf()))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "user", password = "pass")
    public void updateProfilePage() throws Exception {
        mockMvc.perform(post("/user/profile")
                        .param("firstName", "TestFirstName")
                        .param("lastName", "NewTestLastName")
                        .param("username", "user")
                        .param("password", "pass")
                        .with(csrf())
                ).andExpect(status().isOk());
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
}