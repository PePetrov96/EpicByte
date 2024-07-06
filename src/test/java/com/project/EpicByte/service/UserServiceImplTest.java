package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.impl.UserServiceImpl;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.UUID;


import static com.project.EpicByte.util.Constants.*;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    UserRegisterDTO dto;

    @Mock
    RedirectAttributes redirectAttributes;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    HttpServletRequest request;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    UserEntity userEntity;

    @Mock
    MessageSource messageSource;

    @Mock
    Model model;

    @Mock
    Principal principal;

    @Mock
    Breadcrumbs breadcrumbs;

    @InjectMocks
    UserServiceImpl userService;

    UUID globalID = UUID.randomUUID();
    String testUsername = "test_username";
    String testPassword = "test_password";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        this.userService = new UserServiceImpl(
                userRepository,
                modelMapper,
                userRoleRepository,
                passwordEncoder,
                request,
                eventPublisher,
                breadcrumbs);

        // Mock the modelMapper
        this.userEntity = new UserEntity();
        userEntity.setId(globalID);
        userEntity.setUsername(testUsername);
        userEntity.setPassword(passwordEncoder.encode(testPassword));

        when(modelMapper.map(any(UserRegisterDTO.class), eq(UserEntity.class))).thenReturn(userEntity);
    }

    @Test
    public void test_ShowLoginPage() {
        String page = userService.showLoginPage();
        assertEquals(AUTH_LOGIN_HTML, page);
    }

    @Test
    public void test_HandleLoginError() {
        Model model = mock(Model.class);
        userService.handleLoginError(model);
        verify(model, times(1)).addAttribute("loginError", true);
    }

    @Test
    public void test_RegisterUser_invalidInput() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        Model model = mock(Model.class);
        String actualPage = userService.registerUser(dto, model, bindingResult);
        assertEquals(AUTH_REGISTER_HTML, actualPage);
    }

    @Test
    public void test_RegisterUser_validInput() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Model model = mock(Model.class);
        String actualPage = userService.registerUser(new UserRegisterDTO(), model, bindingResult);
        assertEquals("redirect:" + LOGIN_URL, actualPage);
    }

    @Test
    public void userService_GetUserWithCorrectUsername_ShouldReturnCorrect() {
        Mockito.when(this.userRepository
                        .findUserEntityByUsername(testUsername))
                .thenReturn(this.userEntity);
        UserEntity expected = this.userEntity;

        assertEquals(expected, this.userRepository .findUserEntityByUsername(testUsername));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_ShowProfilePage_noUsername() {
        String expectedViewName = "user-profile";
        when(userService.showProfilePage(model, principal)).thenReturn(expectedViewName);

        String actualViewName = userService.showProfilePage(model, principal);

        verify(userService, times(1)).showProfilePage(model, principal);
        Assert.assertEquals(expectedViewName, actualViewName);
    }

}



