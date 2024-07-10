package com.project.EpicByte.service;

import com.project.EpicByte.exceptions.UsernameIsEmptyException;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRole;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.project.EpicByte.model.entity.enums.UserRolesEnum.MODERATOR;
import static com.project.EpicByte.util.Constants.ADMIN_MANAGE_PRIVILEGES_URL;
import static com.project.EpicByte.util.Constants.USER_PRIVILEGE_CONTROLLER_HTML;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    private AdminService adminService;
    private UserEntity userEntity;
    private UserRole userRole;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    Model model;

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        this.adminService = new AdminServiceImpl(userRepository,
                userRoleRepository);

        this.userEntity = new UserEntity();
        userEntity.setUsername("testUsername");
        userEntity.setPassword("testPassword");
        userEntity.setEmail("testEmail");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setTermsAndConditionsAgreed(true);

        this.userRole = new UserRole();
        userRole.setRole(MODERATOR);

    }

    @Test
    public void displayAdminManagePrivilegesPage_success(){
        when(this.userRepository.findAll()).thenReturn(new ArrayList<UserEntity>());

        String result = this.adminService.displayAdminManagePrivilegesPage(model);
        Assertions.assertEquals(USER_PRIVILEGE_CONTROLLER_HTML, result);
    }

    @Test
    public void giveModeratorPrivilegesToUser_success(){
        when(this.userRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        when(this.userRoleRepository.findUserRoleByRole(any())).thenReturn(userRole);

        String result = this.adminService.giveModeratorPrivilegesToUser(UUID.randomUUID(), model);
        Assertions.assertEquals("redirect:" + ADMIN_MANAGE_PRIVILEGES_URL, result);
    }

    @Test
    public void giveModeratorPrivilegesToUser_fail_usernameIsEmpty(){
        try {
            this.adminService.giveModeratorPrivilegesToUser(UUID.randomUUID(), model);
        } catch (UsernameIsEmptyException e) {
            assert true;
        }
    }

    @Test
    public void removeModeratorPrivileges_success(){
        when(this.userRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        when(this.userRoleRepository.findUserRoleByRole(any())).thenReturn(userRole);

        String result = this.adminService.removeModeratorPrivileges(UUID.randomUUID(), model);
        Assertions.assertEquals("redirect:" + ADMIN_MANAGE_PRIVILEGES_URL, result);
    }

    @Test
    public void removeModeratorPrivileges_fail_usernameIsEmpty(){
        try {
            this.adminService.removeModeratorPrivileges(UUID.randomUUID(), model);
        } catch (UsernameIsEmptyException e) {
            assert true;
        }
    }
}
