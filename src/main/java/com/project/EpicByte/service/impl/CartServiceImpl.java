package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.CartItemNotFoundException;
import com.project.EpicByte.exceptions.EmptyCartException;
import com.project.EpicByte.model.bindingModel.CartItemBindingModel;
import com.project.EpicByte.model.bindingModel.UserCartBindingModel;
import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserOrder;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.*;
import com.project.EpicByte.service.CartService;
import com.project.EpicByte.util.Breadcrumbs;
import org.hibernate.Hibernate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import static com.project.EpicByte.util.Constants.*;

@Service
public class CartServiceImpl extends Breadcrumbs implements CartService {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final ToyRepository toyRepository;
    private final MessageSource messageSource;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final UserOrderRepository userOrderRepository;

    public CartServiceImpl(BookRepository bookRepository, TextbookRepository textbookRepository,
                           MovieRepository movieRepository, MusicRepository musicRepository,
                           ToyRepository toyRepository, MessageSource messageSource, CartRepository cartRepository, UserRepository userRepository, UserOrderRepository userOrderRepository) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.toyRepository = toyRepository;
        this.messageSource = messageSource;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userOrderRepository = userOrderRepository;
    }

    @Override
    public String showCartPage(String username, Model model) {
        try {
            UserEntity userEntity = getUserEntityByUsername(username);
            setModelAttributesForCart(userEntity, model);
            addProductBreadcrumb(model, USER_CART_URL, "Cart");
        } catch (UsernameNotFoundException e) {
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

        return "redirect:/user/cart";
    }

    @Override
    public ResponseEntity<String> addProductToCart(Map<String, Object> payload, Principal principal) {
        try {
            UUID productId = UUID.fromString(payload.get("productId").toString());
            String productType = payload.get("productType").toString();
            addToCart(productId, productType, principal);
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

            setModelAttributesForCart(userEntity, model);

            model.addAttribute("orderAddressDTO", new OrderAddressDTO());
            addProductBreadcrumb(model, USER_CART_URL, "Cart", "Checkout");
            return CART_CHECKOUT_HTML;
        } catch (NullPointerException | UsernameNotFoundException exception) {
            return returnErrorPage(model);
        }
    }

    @Override
    public String confirmCheckout(OrderAddressDTO orderAddressDTO, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            UserEntity userEntity = getUserEntityByUsername(principal.getName());
            setModelAttributesForCart(userEntity, model);
            addProductBreadcrumb(model, USER_CART_URL, "Cart", "Checkout");
            return CART_CHECKOUT_HTML;
        }

        try {
            createUserOrder(principal.getName(), orderAddressDTO);
            model.addAttribute("pageType", "Completed Successfully");
            model.addAttribute("pageText", getLocalizedText("order.successfully.received.text"));
            return DISPLAY_TEXT_HTML;
        } catch (EmptyCartException exception) {
            return INDEX_HTML;
        }
    }

    //Support methods
    private void createUserOrder(String username, OrderAddressDTO orderAddressDTO) {
        UserEntity userEntity = getUserEntityByUsername(username);
        UserOrder order = initializeUserOrder(userEntity, orderAddressDTO);

        List<BaseProduct> products = fetchUserCartProducts(userEntity);

        if (products.isEmpty()) {
            throw new EmptyCartException();
        }

        // Calculate total cost
        BigDecimal totalCost = products.stream()
                .map(BaseProduct::getProductPrice)
                .reduce(order.getTotalCost(), BigDecimal::add);
        order.setTotalCost(totalCost);

        List<OrderItem> orderItems = createOrderItems(order, products);
        order.setOrderItems(orderItems);
        finalizeOrderCreation(order, userEntity);
    }

    private List<OrderItem> createOrderItems(UserOrder userOrder, List<BaseProduct> products) {
        Map<BaseProduct, Integer> productCountMap = new HashMap<>();

        for (BaseProduct product : products) {
            productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (BaseProduct product : productCountMap.keySet()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            int quantity = productCountMap.get(product);
            orderItem.setQuantity(quantity);
            orderItem.setTotalProductPrice(product.getProductPrice().multiply(new BigDecimal(quantity)));
            orderItem.setUserOrder(userOrder);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    protected void finalizeOrderCreation(UserOrder userOrder, UserEntity userEntity) {
        userOrderRepository.save(userOrder);
        userEntity.getCartItems().clear();
        userRepository.save(userEntity);
    }

    private List<BaseProduct> fetchUserCartProducts(UserEntity userEntity) {
        return this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();
    }


    private UserOrder initializeUserOrder(UserEntity userEntity, OrderAddressDTO orderAddressDTO) {
        UserOrder order = new UserOrder();
        order.setUser(userEntity);
        order.setCity(orderAddressDTO.getCity());
        order.setNeighborhood(orderAddressDTO.getNeighborhood());
        order.setAddress(orderAddressDTO.getAddress());
        order.setComplete(false);  // default orders are "not shipped"
        order.setOrderDate(LocalDate.now());
        order.setTotalCost(new BigDecimal("4.99"));
        return order;
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private String returnErrorPage(Model model) {
        model.addAttribute("errorType", "Oops...");
        model.addAttribute("errorText", "Something went wrong!");
        return ERROR_PAGE_HTML;
    }

    public void addToCart(UUID productId, String productType, Principal principal) {
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

    private void setModelAttributesForCart(UserEntity userEntity, Model model) {
        List<BaseProduct> products = this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();

        UserCartBindingModel userCartBindingModel = new UserCartBindingModel();

        // Using a map, count the quantity of each unique product
        LinkedHashMap<BaseProduct, Integer> productCountMap = new LinkedHashMap<>();
        for (BaseProduct product : products) {
            productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
        }

        // Convert each unique product to CartItemDTO and set the quantity
        for (BaseProduct product : productCountMap.keySet()) {
            CartItemBindingModel cartItemBindingModel = new CartItemBindingModel();
            int quantity = productCountMap.get(product);
            BigDecimal productPrice = product.getProductPrice();

            cartItemBindingModel.setId(product.getId());
            cartItemBindingModel.setProductType(product.getProductType().toString().toLowerCase());
            cartItemBindingModel.setProductImageUrl(product.getProductImageUrl());
            cartItemBindingModel.setProductName(product.getProductName());
            cartItemBindingModel.setProductPrice(product.getProductPrice());
            cartItemBindingModel.setQuantity(quantity);
            cartItemBindingModel.setTotalPriceOfProduct(productPrice.multiply(BigDecimal.valueOf(quantity)));

            userCartBindingModel.getCartItems().add(cartItemBindingModel);
        }

        // setting the total price of all items in the cart
        BigDecimal totalPrice = userCartBindingModel.getCartItems().stream()
                .map(CartItemBindingModel::getTotalPriceOfProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        userCartBindingModel.setTotalPrice(totalPrice);

        model.addAttribute("cartItemList", userCartBindingModel);
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

    private UserEntity getUserEntityByUsername(String username) {
        UserEntity user = this.userRepository
                .findUserEntityByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

//        Hibernate.initialize(user.getCartItems());

        return user;
    }
}
