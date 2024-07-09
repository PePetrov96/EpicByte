package com.project.EpicByte.service;

import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserOrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.*;
import com.project.EpicByte.service.impl.CartServiceImpl;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.*;

import static com.project.EpicByte.util.Constants.CART_HTML;
import static com.project.EpicByte.util.Constants.USER_CART_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    UserOrderRepository userOrderRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    TextbookRepository textbookRepository;
    @Mock
    MovieRepository movieRepository;
    @Mock
    MusicRepository musicRepository;
    @Mock
    ToyRepository toyRepository;

    @Mock
    Model model;

    @Mock
    Principal principal;

    @Mock
    HttpSession session;

    CartService cartService;
    Breadcrumbs breadcrumbs;
    UserEntity userEntity;
    BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        this.breadcrumbs = new Breadcrumbs(messageSource);

        this.cartService = new CartServiceImpl(bookRepository,
                textbookRepository,
                movieRepository,
                musicRepository,
                toyRepository,
                cartRepository,
                userRepository,
                modelMapper,
                breadcrumbs);

        this.userEntity = new UserEntity();
        userEntity.setUsername("testUsername");
        userEntity.setPassword("testPassword");
        userEntity.setEmail("testEmail");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setTermsAndConditionsAgreed(true);

        this.session = Mockito.mock(HttpSession.class);
        this.model = Mockito.mock(Model.class);
    }

    @Test
    public void showCartPage_success() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        String result = this.cartService.showCartPage("username", model, session);
        Assertions.assertEquals(CART_HTML, result);
    }

    @Test
    public void deleteItemFromUserCart_success() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        when(this.cartRepository.findAllByUserIdAndProductId(any(), any())).thenReturn(new ArrayList<>());
        when(session.getAttribute("numItems")).thenReturn(1);

        String result = this.cartService.deleteItemFromUserCart(UUID.randomUUID(), "username", model, session);
        Assertions.assertEquals("redirect:" + USER_CART_URL, result);
    }

    @Test
    public void addProductToCart_fail_noUser_Book() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "BOOK");

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void addProductToCart_success_Book() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "BOOK");
        when(session.getAttribute("numItems")).thenReturn(1);

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void addProductToCart_fail_noUser_Textbook() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "TEXTBOOK");

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void addProductToCart_success_Textbook() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "TEXTBOOK");
        when(session.getAttribute("numItems")).thenReturn(1);

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void addProductToCart_fail_noUser_Music() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "MUSIC");

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void addProductToCart_success_Music() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "MUSIC");
        when(session.getAttribute("numItems")).thenReturn(1);

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void addProductToCart_fail_noUser_Movie() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "MOVIE");

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void addProductToCart_success_Movie() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "MOVIE");
        when(session.getAttribute("numItems")).thenReturn(1);

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void addProductToCart_fail_noUser_Toy() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "TOY");

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    public void addProductToCart_success_Toy() {
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "TOY");
        when(session.getAttribute("numItems")).thenReturn(1);

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, principal, session);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void addProductToCart_fail_emptyPrincipal() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("productId", UUID.randomUUID().toString());
        payload.put("productType", "TOY");

        ResponseEntity<String> result = this.cartService.addProductToCart(payload, null, session);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
