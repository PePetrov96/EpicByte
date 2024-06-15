package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.CartItemNotFoundException;
import com.project.EpicByte.exceptions.EmptyCartException;
import com.project.EpicByte.model.dto.productDTOs.CartItemDTO;
import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.model.dto.productDTOs.UserCartDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.Order;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.repository.productRepositories.*;
import com.project.EpicByte.service.CartService;
import com.project.EpicByte.util.Breadcrumbs;
import org.hibernate.Hibernate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.EpicByte.util.Constants.*;

@Transactional
@Service
public class CartServiceImpl extends Breadcrumbs implements CartService {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;
    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final MessageSource messageSource;


    public CartServiceImpl(BookRepository bookRepository, TextbookRepository textbookRepository,
                           MovieRepository movieRepository, MusicRepository musicRepository,
                           ToyRepository toyRepository, OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository, MessageSource messageSource) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    @Override
    public String showCartPage(Principal principal, Model model) {
        try {
            UserEntity userEntity = getUserEntityByUsername(principal.getName());
            setModelAttributeOfUserCart(userEntity, model);
            addProductBreadcrumb(model, USER_CART_URL, "Cart");
        } catch (NullPointerException | UsernameNotFoundException e) {
            model.addAttribute("errorType", "Oops...");
            model.addAttribute("errorText", "Something went wrong!");
            return ERROR_PAGE_HTML;
        }
        return CART_HTML;
    }

    @Override
    public String deleteItemFromUserCart(UUID productId, String username, Model model) {
        UserEntity userEntity = getUserEntityByUsername(username);
        List<CartItem> deletionProducts = this.cartRepository.findAllByUserIdAndProductId(userEntity.getId(), productId);
        this.cartRepository.deleteAll(deletionProducts);
        return "redirect:" + USER_CART_URL;
    }

    @Override
    public ResponseEntity<String> addProductToCart(Map<String, Object> payload, Principal principal) {
        try {
            UUID productId = UUID.fromString(payload.get("productId").toString());
            String productType = payload.get("productType").toString();
            addCartItemToDatabase(productId, productType, principal);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (UsernameNotFoundException | CartItemNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding product to cart");
        }
    }

    @Override
    public String showCartCheckoutPage(Principal principal, Model model) {
        try {
            UserEntity userEntity = getUserEntityByUsername(principal.getName());
            setModelAttributeOfUserCart(userEntity, model);
            model.addAttribute("orderAddressDTO", new OrderAddressDTO());
            addProductBreadcrumb(model, USER_CART_URL, "Cart", "Checkout");
            return CART_CHECKOUT_HTML;
        } catch (NullPointerException | UsernameNotFoundException exception) {
            model.addAttribute("errorType", "Oops...");
            model.addAttribute("errorText", "Something went wrong!");
            return ERROR_PAGE_HTML;
        }
    }

    @Override
    public String confirmCheckout(OrderAddressDTO orderAddressDTO, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            UserEntity userEntity = getUserEntityByUsername(principal.getName());
            setModelAttributeOfUserCart(userEntity, model);
            addProductBreadcrumb(model, USER_CART_URL, "Cart", "Checkout");
            return CART_CHECKOUT_HTML;
        }

        try {
            createOrder(principal.getName(), orderAddressDTO);
            model.addAttribute("pageType", "Completed Successfully");
            model.addAttribute("pageText", getLocalizedText("order.successfully.received.text"));
            return DISPLAY_TEXT_HTML;
        } catch (EmptyCartException exception) {
            return INDEX_HTML;
        }
    }

    private void createOrder(String username, OrderAddressDTO orderAddressDTO) {
        UserEntity userEntity = getUserEntityByUsername(username);

        Order order = new Order();
        order.setUser(userEntity);

        order.setCity(orderAddressDTO.getCity());
        order.setNeighborhood(orderAddressDTO.getNeighborhood());
        order.setAddress(orderAddressDTO.getAddress());

        order.setComplete(false);

        List<BaseProduct> products = this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();

        if (products.isEmpty()) {
            throw new EmptyCartException();
        }

        List<OrderItem> orderItems = products.stream().map(baseProduct -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(baseProduct);
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        orderRepository.save(order);

        userEntity.getOrders().add(order);
        userEntity.getCartItems().clear();

        userRepository.save(userEntity);
    }

    //Support methods
    public void addCartItemToDatabase(UUID productId, String productType, Principal principal) {
        CartItem cartItem = new CartItem();

        if (principal == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        UserEntity userEntity = getUserEntityByUsername(principal.getName());

        cartItem.setUser(userEntity);

        switch (productType) {
            case "BOOK": cartItem.setProduct(getBookById(productId)); break;
            case "TEXTBOOK": cartItem.setProduct(getTextbookById(productId)); break;
            case "MOVIE": cartItem.setProduct(getMovieById(productId)); break;
            case "MUSIC": cartItem.setProduct(getMusicById(productId)); break;
            case "TOY": cartItem.setProduct(getToyById(productId)); break;
        }

        this.cartRepository.saveAndFlush(cartItem);
    }

    private void setModelAttributeOfUserCart(UserEntity userEntity, Model model) {
        List<BaseProduct> products = this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();


        if (products.isEmpty()) {
            model.addAttribute("emptyCart", true);
            return;
        }

        UserCartDTO userCartDTO = new UserCartDTO();

        LinkedHashMap<BaseProduct, Integer> productCountMap = new LinkedHashMap<>();
        for (BaseProduct product : products) {
            productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
        }

        for (BaseProduct product : productCountMap.keySet()) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            int quantity = productCountMap.get(product);
            BigDecimal productPrice = product.getProductPrice();

            cartItemDTO.setId(product.getId());
            cartItemDTO.setProductType(product.getProductType().toString().toLowerCase());
            cartItemDTO.setProductImageUrl(product.getProductImageUrl());
            cartItemDTO.setProductName(product.getProductName());
            cartItemDTO.setProductPrice(product.getProductPrice());
            cartItemDTO.setQuantity(quantity);
            cartItemDTO.setTotalPriceOfProduct(productPrice.multiply(BigDecimal.valueOf(quantity)));

            userCartDTO.getCartItems().add(cartItemDTO);
        }

        BigDecimal totalPrice = userCartDTO.getCartItems().stream()
                .map(CartItemDTO::getTotalPriceOfProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        userCartDTO.setTotalPrice(totalPrice);

        model.addAttribute("cartItemList", userCartDTO);
        model.addAttribute("emptyCart", false);
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private Book getBookById(UUID productId) {
        return this.bookRepository.findBookById(productId);
    }

    private Textbook getTextbookById(UUID productId) {
        return this.textbookRepository.findTextbookById(productId);
    }

    private Movie getMovieById(UUID productId) {
        return this.movieRepository.findMovieById(productId);
    }

    private Music getMusicById(UUID productId) {
        return this.musicRepository.findMusicById(productId);
    }

    private Toy getToyById(UUID productId) {
        return this.toyRepository.findToyById(productId);
    }

    @Transactional
    protected UserEntity getUserEntityByUsername(String username) {
        UserEntity user = this.userRepository
                .findUserEntityByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Hibernate.initialize(user.getCartItems());
        Hibernate.initialize(user.getOrders());

        return user;
    }
}
