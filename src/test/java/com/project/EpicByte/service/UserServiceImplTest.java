package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.service.impl.UserServiceImpl;
import com.project.EpicByte.util.Breadcrumbs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import static com.project.EpicByte.util.Constants.*;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

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
    Breadcrumbs breadcrumbs;

    @Before
    public void setUp() {
        this.userService = new UserServiceImpl(userRepository, modelMapper, userRoleRepository, passwordEncoder, request, eventPublisher);

        this.breadcrumbs = new Breadcrumbs();
        breadcrumbs.setMessageSource(messageSource);

        // Mock the modelMapper
        when(modelMapper.map(any(UserRegisterDTO.class), eq(UserEntity.class))).thenReturn(userEntity);
    }

    @Test
    public void testShowLoginPage() {
        String page = userService.showLoginPage();
        assertEquals(AUTH_LOGIN_HTML, page);
    }

    @Test
    public void testHandleLoginError() {
        Model model = mock(Model.class);
        userService.handleLoginError(model);
        verify(model, times(1)).addAttribute("loginError", true);
    }

    @Test
    public void testRegisterUser_hasErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = mock(Model.class);
        String page = userService.registerUser(new UserRegisterDTO(), model, bindingResult);
        assertEquals(AUTH_REGISTER_HTML, page);
    }

    @Test
    public void testRegisterUser_noErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        String expectedPage = "redirect:" + LOGIN_URL;
        assertEquals(expectedPage, userService.registerUser(dto, redirectAttributes, bindingResult));
    }
}
