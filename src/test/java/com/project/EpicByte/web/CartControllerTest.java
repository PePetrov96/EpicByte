package com.project.EpicByte.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.bindingModel.CartItemBindingModel;
import com.project.EpicByte.model.bindingModel.UserCartBindingModel;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.UserRoleRepository;
import com.project.EpicByte.repository.productRepositories.BookRepository;
import org.hibernate.Hibernate;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

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

    private UUID userID;
    private UUID productID;
    private final MockHttpSession session = new MockHttpSession();

    @BeforeEach
    void setUp() {
        session.setAttribute("numItems", 1);

        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);

        if (userRepository.count() == 0) {
            userRepository.save(returnUser());
        }

        if (bookRepository.count() == 0) {
            bookRepository.save(returnBook());
        }

        userID = returnUserId();
        productID = returnProductId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "user")
    void displayCartPage_emptyCart() throws Exception {
        removeProductFromUserCart();

        mockMvc.perform(get(USER_CART_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("emptyCart"))
                .andExpect(model().attributeExists("breadcrumbs"))
                .andExpect(view().name(CART_HTML));
    }

    @Test
    @WithMockUser(username = "user")
    void displayCartPage_fullCart() throws Exception {
        addProductToUserCart();

        mockMvc.perform(get(USER_CART_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("breadcrumbs"))
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(view().name(CART_HTML));
    }

    @Test
    @WithMockUser(username = "user")
    void handleItemDeletionFromCart() throws Exception {
        addProductToUserCart();

        mockMvc.perform(post(USER_CART_DELETE_URL + productID)
                        .with(csrf())
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + USER_CART_URL));
    }

    @Test
    @WithMockUser(username = "user")
    void addItemToUserCart_success_realUser() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", productID.toString());
        payload.put("productType", "BOOK");

        ObjectMapper objectMapper = new ObjectMapper();
        String payloadJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post(PRODUCT_ADD_TO_CART_URL)
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin")
    void addItemToUserCart_fail_AdminUser() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", productID.toString());
        payload.put("productType", "BOOK");

        ObjectMapper objectMapper = new ObjectMapper();
        String payloadJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post(PRODUCT_ADD_TO_CART_URL)
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user")
    void displayCartCheckoutPage() throws Exception {
        UserCartBindingModel userCartBindingModel = new UserCartBindingModel();
        userCartBindingModel.setTotalPrice(new BigDecimal("100.00"));
        userCartBindingModel.getCartItems().add(new CartItemBindingModel());

        session.setAttribute("userCartBindingModel", userCartBindingModel);

        mockMvc.perform(get(USERS_CART_CHECKOUT_URL)
                        .with(csrf())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(model().attributeExists("orderAddressDTO"))
                .andExpect(view().name(CART_CHECKOUT_HTML));
    }

    @Test
    @WithMockUser(username = "user")
    void handleCartCheckoutConfirmation_fail_bindingResultHasErrors() throws Exception {
        UserCartBindingModel userCartBindingModel = new UserCartBindingModel();
        userCartBindingModel.setTotalPrice(new BigDecimal("100.00"));
        userCartBindingModel.getCartItems().add(new CartItemBindingModel());

        session.setAttribute("userCartBindingModel", userCartBindingModel);

        mockMvc.perform(post(USERS_CART_CHECKOUT_URL)
                        .with(csrf())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartItemList"))
                .andExpect(model().attributeExists("orderAddressDTO"))
                .andExpect(view().name(CART_CHECKOUT_HTML));
    }

    @Test
    @WithMockUser(username = "user")
    void displayConfirmCheckoutPage() throws Exception {
        mockMvc.perform(get(USERS_CART_CHECKOUT_CONFIRM_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("breadcrumbs"))
                .andExpect(model().attributeExists("pageType"))
                .andExpect(model().attributeExists("pageText"))
                .andExpect(view().name(DISPLAY_TEXT_HTML));
    }

    private void addProductToUserCart() {
        UserEntity userEntity = this.userRepository.findUserEntityById(userID);

        if (this.cartRepository.count() == 0) {
            CartItem cartItem = new CartItem();
            cartItem.setUser(userEntity);
            cartItem.setProduct(this.bookRepository.findBookById(productID));
            this.cartRepository.saveAndFlush(cartItem);
        }
    }

    private void removeProductFromUserCart() {
        if (this.cartRepository.count() != 0) {
            this.cartRepository.deleteAll();
        }
    }

    private UserEntity returnUser() {
        UserEntity user = new UserEntity();
        user.setId(userID);
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setEmail("test@softuni.bg");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setRoles(new HashSet<>());
        return user;
    }

    private Book returnBook() {
        Book book = new Book();

        book.setDateCreated(LocalDate.now());
        book.setNewProduct(true);
        book.setProductType(ProductTypeEnum.BOOK);
        book.setProductImageUrl("path/to/image.jpg");
        book.setProductName("New Book");
        book.setProductPrice(BigDecimal.valueOf(25.99));
        book.setDescription("This is a book description.");
        book.setAuthorName("John Doe");
        book.setPublisher("Pearson");
        book.setPublicationDate(LocalDate.of(2022, 1, 1));
        book.setLanguage(LanguageEnum.English);
        book.setPrintLength(300);
        book.setDimensions("20 x 15 x 3");

        return book;
    }

    private UUID returnUserId() {
        return this.userRepository.findAll().get(0).getId();
    }

    private UUID returnProductId() {
        return this.bookRepository.findAll().get(0).getId();
    }

    private UUID returnCartItemId() {
        return this.cartRepository.findAll().get(0).getId();
    }
}