package com.project.EpicByte.service;

import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserOrder;
import com.project.EpicByte.repository.UserOrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.impl.UserOrderServiceImpl;
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
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserOrderServiceTest {
    UserOrderService userOrderService;
    Breadcrumbs breadcrumbs;
    MessageSource messageSource;
    ModelMapper modelMapper;
    Model model;
    HttpSession session;
    UserEntity userEntity;

    @Mock
    UserOrderRepository userOrderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    Principal principal;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        this.breadcrumbs = new Breadcrumbs(messageSource);

        this.userOrderService = new UserOrderServiceImpl(
                userOrderRepository,
                userRepository,
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
    public void displayUserOrders_success() {
        when(this.userRepository.findUserByUsernameWithInitializedOrders(any())).thenReturn(userEntity);
        when(this.userOrderRepository.findUserOrderByUserIdOrderByOrderDateDesc(any())).thenReturn(new HashSet<UserOrder>());

        String result = this.userOrderService.displayUserOrders(model, principal);
        Assertions.assertEquals(ORDERS_HTML, result);
    }

    @Test
    public void displayAdminAllUserOrders_success() {
        when(this.userOrderRepository.findUserOrdersIncompleteOrderByOrderDateDesc()).thenReturn(new HashSet<UserOrder>());

        String result = this.userOrderService.displayAdminAllUserOrders(model);
        Assertions.assertEquals(ORDERS_HTML, result);
    }

    @Test
    public void completeUserOrder_success() {
        when(this.userOrderRepository.findUserOrderById(any())).thenReturn(Optional.of(new UserOrder()));

        String result = this.userOrderService.completeUserOrder(UUID.randomUUID());
        Assertions.assertEquals("redirect:" + MODERATOR_ORDERS_URL, result);
    }

    @Test
    public void displayUserOrderDetails_success_existingUserOrder() {
        when(this.userOrderRepository.findUserOrderById(any())).thenReturn(Optional.of(new UserOrder()));

        String result = this.userOrderService.displayUserOrderDetails(UUID.randomUUID(), model);
        Assertions.assertEquals(ORDER_DETAILS_HTML, result);
    }

    @Test
    public void displayUserOrderDetails_success_nonExistingUserOrder() {
        when(this.userOrderRepository.findUserOrderById(any())).thenReturn(Optional.empty());

        String result = this.userOrderService.displayUserOrderDetails(UUID.randomUUID(), model);
        Assertions.assertEquals("redirect:" + INDEX_URL, result);
    }
}
