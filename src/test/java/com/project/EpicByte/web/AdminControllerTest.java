package com.project.EpicByte.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.model.entity.enums.UserRolesEnum;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
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

import java.util.HashSet;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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

        if (userRoleRepository.count() == 0) {
            userRoleRepository.save(returnModeratorRole());
        }

        uuid = returnUserId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayAdminManagePrivilegesPage() throws Exception {
        mockMvc.perform(get(ADMIN_MANAGE_PRIVILEGES_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_PRIVILEGE_CONTROLLER_HTML));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void giveModeratorPrivileges() throws Exception {
        mockMvc.perform(post(ADMIN_GIVE_PRIVILEGES_URL + uuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + ADMIN_MANAGE_PRIVILEGES_URL));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void removeModeratorPrivileges() throws Exception {
        UserEntity user = this.userRepository.findUserEntityById(uuid);
        user.getRoles().add(this.userRoleRepository.findUserRoleByRole(UserRolesEnum.MODERATOR));
        this.userRepository.saveAndFlush(user);

        mockMvc.perform(post(ADMIN_REMOVE_PRIVILEGES_URL + uuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + ADMIN_MANAGE_PRIVILEGES_URL));
    }

    private UserEntity returnUser() {
        UserEntity user = new UserEntity();
        user.setId(uuid);
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("test@softuni.bg");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setRoles(new HashSet<>());
        return user;
    }

    private UUID returnUserId() {
        return this.userRepository.findAll().get(0).getId();
    }

    private UserRole returnModeratorRole() {
        UserRole userRole = new UserRole();
        userRole.setRole(UserRolesEnum.MODERATOR);
        return userRole;
    }

}