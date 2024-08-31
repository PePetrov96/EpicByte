package com.project.EpicByte.service;

import com.project.EpicByte.model.bindingModel.UserCartBindingModel;
import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.impl.CartCheckoutServiceImpl;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartCheckoutServiceTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    Model model;

    @Mock
    Principal principal;

    @Mock
    HttpSession session;

    CartCheckoutService cartCheckoutService;
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

        this.cartCheckoutService = new CartCheckoutServiceImpl(cartRepository,
                userRepository,
                messageSource,
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
    public void showCartCheckoutPage_success() {
        session.setAttribute("userCartBindingModel", new UserCartBindingModel());
        String result = this.cartCheckoutService.showCartCheckoutPage(principal, model, session);
        Assertions.assertEquals(CART_CHECKOUT_HTML, result);
    }

    @Test
    public void confirmCheckout_fail_bindingResultHasErrors() {
        this.bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = this.cartCheckoutService.confirmCheckout(new OrderAddressDTO(),
                bindingResult,
                principal,
                model,
                session);

        Assertions.assertEquals(CART_CHECKOUT_HTML, result);
    }

    @Test
    public void confirmCheckout_success_emptyCart() {
        this.bindingResult = mock(BindingResult.class);
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);

        String result = this.cartCheckoutService.confirmCheckout(new OrderAddressDTO(),
                bindingResult,
                principal,
                model,
                session);

        Assertions.assertEquals(CART_HTML, result);
    }

    @Test
    public void confirmCheckout_success_finishOrder() {
        this.bindingResult = mock(BindingResult.class);
        when(this.userRepository.findUserEntityByUsername(any())).thenReturn(userEntity);

        List<CartItem> items = new ArrayList<>();

        Toy toy = new Toy();
        toy.setProductPrice(new BigDecimal("100.00"));

        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(toy);
        cartItem1.setUser(userEntity);
        cartItem1.setId(UUID.randomUUID());
        items.add(cartItem1);

        when(this.cartRepository.findAllByUserId(any())).thenReturn(items);

        when(this.userRepository.findUserByUsernameWithInitializedCartItems(any())).thenReturn(userEntity);

        String result = this.cartCheckoutService.confirmCheckout(new OrderAddressDTO(),
                bindingResult,
                principal,
                model,
                session);

        Assertions.assertEquals(DISPLAY_TEXT_HTML, result);
    }

    @Test
    public void displayCartCheckoutConfirmationPage_success() {
        String result = this.cartCheckoutService.displayCartCheckoutConfirmationPage(model);
        Assertions.assertEquals(DISPLAY_TEXT_HTML, result);
    }
}
