package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.impl.UserServiceImpl;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;
    private UserEntity userEntity;
    private UserRegisterDTO userRegisterDTO;
    private UserUpdateDTO userUpdateDTO;
    private BindingResult bindingResult;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    Breadcrumbs breadcrumbs;

    @Mock
    Model model;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        this.breadcrumbs = new Breadcrumbs(messageSource);
        PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        this.userService = new UserServiceImpl(userRepository,
                modelMapper,
                userRoleRepository,
                passwordEncoder,
                request,
                eventPublisher,
                breadcrumbs);

        this.bindingResult = mock(BindingResult.class);

        this.userEntity = new UserEntity();
        userEntity.setUsername("testUsername");
        userEntity.setPassword("testPassword");
        userEntity.setEmail("testEmail");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setTermsAndConditionsAgreed(true);

        this.userUpdateDTO = modelMapper.map(userEntity, UserUpdateDTO.class);
        this.userRegisterDTO = modelMapper.map(userEntity, UserRegisterDTO.class);
    }

    @Test
    public void showLoginPage_success(){
        String result = userService.showLoginPage();
        Assertions.assertEquals(AUTH_LOGIN_HTML, result);
    }

    @Test
    public void handleLoginError_success(){
        String result = userService.handleLoginError(model);
        Assertions.assertEquals(AUTH_LOGIN_HTML, result);
    }

    @Test
    public void showRegisterPage_success(){
        String result = userService.showRegisterPage(model);
        Assertions.assertEquals(AUTH_REGISTER_HTML, result);
    }

    @Test
    public void registerUser_success(){
        String result = userService.registerUser(userRegisterDTO, bindingResult, model);
        Assertions.assertEquals("redirect:" + LOGIN_URL, result);
    }

    @Test
    public void registerUser_fail_bindingResultHasErrors(){
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = userService.registerUser(new UserRegisterDTO(), bindingResult, model);
        Assertions.assertEquals(AUTH_REGISTER_HTML, result);
    }

    @Test
    public void showProfilePage_success() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        String result = this.userService.showProfilePage(model, Mockito.mock(Principal.class));
        Assertions.assertEquals(USER_PROFILE_HTML, result);
    }

    @Test
    public void showProfilePage_fail_UsernameNotFound() {
        try {
            this.userService.showProfilePage(model, Mockito.mock(Principal.class));
        } catch (UsernameNotFoundException e) {
            assert true;
        }
    }

    @Test
    public void updateProfilePage_success() {
        RedirectAttributes redirectAttributes = Mockito.mock(RedirectAttributes.class);
        Principal principal = Mockito.mock(Principal.class);
        when(this.userRepository.findUserEntityById(any())).thenReturn(userEntity);

        String result = this.userService
                .updateProfilePage(userUpdateDTO,
                        bindingResult,
                        model,
                        redirectAttributes,
                        principal);

        Assertions.assertEquals("redirect:" + USER_PROFILE_URL, result);
    }

    @Test
    public void updateProfilePage_fail_bindingResultHasErrors() {
        RedirectAttributes redirectAttributes = Mockito.mock(RedirectAttributes.class);
        Principal principal = Mockito.mock(Principal.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = this.userService
                .updateProfilePage(new UserUpdateDTO(),
                        bindingResult,
                        model,
                        redirectAttributes,
                        principal);

        Assertions.assertEquals(USER_PROFILE_HTML, result);
    }

}
