package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.CartItemNotFoundException;
import com.project.EpicByte.exceptions.EmptyCartException;
import com.project.EpicByte.model.bindingModel.CartItemBindingModel;
import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.model.bindingModel.UserCartBindingModel;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.Order;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.repository.*;
import com.project.EpicByte.service.CartService;
import com.project.EpicByte.util.Breadcrumbs;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.EpicByte.util.Constants.*;

@Transactional
@Service
public class CartServiceImpl extends Breadcrumbs implements CartService {
    private final Map<String, JpaRepository<? extends BaseProduct, UUID>> productRepositories;
    private final MessageSource messageSource;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CartServiceImpl(BookRepository bookRepository,
                           TextbookRepository textbookRepository,
                           MovieRepository movieRepository,
                           MusicRepository musicRepository,
                           ToyRepository toyRepository,
                           OrderRepository orderRepository,
                           CartRepository cartRepository,
                           UserRepository userRepository,
                           MessageSource messageSource){
        this.messageSource = messageSource;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        productRepositories = Map.of(
                "BOOK", bookRepository,
                "TEXTBOOK", textbookRepository,
                "MOVIE", movieRepository,
                "MUSIC", musicRepository,
                "TOY", toyRepository
        );
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

    // SUPPORT METHODS

    // Create order and save it to the database for the "confirmCheckout" method.
    private void createOrder(String username, OrderAddressDTO orderAddressDTO) {
        UserEntity userEntity = getUserEntityByUsername(username);
        Order order = initializeOrder(userEntity, orderAddressDTO);

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

    private Order initializeOrder(UserEntity userEntity, OrderAddressDTO orderAddressDTO) {
        Order order = new Order();
        order.setUser(userEntity);
        order.setCity(orderAddressDTO.getCity());
        order.setNeighborhood(orderAddressDTO.getNeighborhood());
        order.setAddress(orderAddressDTO.getAddress());
        order.setComplete(false);  // default orders are "not shipped"
        order.setOrderDate(LocalDate.now());
        order.setTotalCost(new BigDecimal("4.99"));
        return order;
    }

//    private List<OrderItem> createOrderItems(Order order, List<BaseProduct> products) {
//        return products.stream().map(baseProduct -> {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(baseProduct);
//            return orderItem;
//        }).collect(Collectors.toList());
//    }
    private List<OrderItem> createOrderItems(Order order, List<BaseProduct> products) {
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
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private void finalizeOrderCreation(Order order, UserEntity userEntity) {
        orderRepository.save(order);
        userEntity.getOrders().add(order);
        userEntity.getCartItems().clear();
        userRepository.save(userEntity);
    }

    // Add cart item, to the database, linking it to a user, for the "addProductToCart" method.
    public void addCartItemToDatabase(UUID productId, String productType, Principal principal) {
        CartItem cartItem = new CartItem();
        if (principal == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        UserEntity userEntity = getUserEntityByUsername(principal.getName());
        cartItem.setUser(userEntity);

        JpaRepository<? extends BaseProduct, UUID> productRepository = this.productRepositories.get(productType);
        if (productRepository == null) {
            throw new IllegalArgumentException("Invalid product type.");
        }

        Optional<? extends BaseProduct> product = productRepository.findById(productId);
        if (product.isPresent()) {
            cartItem.setProduct(product.get());
        } else {
            throw new NoSuchElementException("No product found with the provided id.");
        }

        this.cartRepository.saveAndFlush(cartItem);
    }

    // Create a User cart entity, that contains the items, in a unique list, counting the number of occurances
    //
    private void setModelAttributeOfUserCart(UserEntity userEntity, Model model) {
        List<BaseProduct> products = fetchUserCartProducts(userEntity);

        if (isUserCartEmpty(products)) {
            model.addAttribute("emptyCart", true);
            return;
        }

        Map<BaseProduct, Integer> productCountMap = calculateProductCounts(products);
        UserCartBindingModel userCartBindingModel = populateUserCartDto(productCountMap);
        model.addAttribute("cartItemList", userCartBindingModel);
        model.addAttribute("emptyCart", false);
    }

    private List<BaseProduct> fetchUserCartProducts(UserEntity userEntity) {
        return this.cartRepository
                .findAllByUserId(userEntity.getId())
                .stream()
                .map(CartItem::getProduct)
                .toList();
    }

    private boolean isUserCartEmpty(List<BaseProduct> products) {
        return products.isEmpty();
    }

    private LinkedHashMap<BaseProduct, Integer> calculateProductCounts(List<BaseProduct> products) {
        LinkedHashMap<BaseProduct, Integer> productCountMap = new LinkedHashMap<>();
        for (BaseProduct product : products) {
            productCountMap.put(product, productCountMap.getOrDefault(product, 0) + 1);
        }
        return productCountMap;
    }

    private UserCartBindingModel populateUserCartDto(Map<BaseProduct, Integer> productCountMap) {
        UserCartBindingModel userCartBindingModel = new UserCartBindingModel();

        for (BaseProduct product : productCountMap.keySet()) {
            CartItemBindingModel cartItemBindingModel = createCartItemDto(productCountMap, product);
            userCartBindingModel.getCartItems().add(cartItemBindingModel);
        }

        userCartBindingModel.setTotalPrice(calculateTotalPrice(userCartBindingModel));

        return userCartBindingModel;
    }

    private CartItemBindingModel createCartItemDto(Map<BaseProduct, Integer> productCountMap, BaseProduct product) {
        int quantity = productCountMap.get(product);
        BigDecimal productPrice = product.getProductPrice();

        CartItemBindingModel cartItemBindingModel = new CartItemBindingModel();
        cartItemBindingModel.setId(product.getId());
        cartItemBindingModel.setProductType(product.getProductType().toString().toLowerCase());
        cartItemBindingModel.setProductImageUrl(product.getProductImageUrl());
        cartItemBindingModel.setProductName(product.getProductName());
        cartItemBindingModel.setProductPrice(productPrice);
        cartItemBindingModel.setQuantity(quantity);
        cartItemBindingModel.setTotalPriceOfProduct(productPrice.multiply(BigDecimal.valueOf(quantity)));

        return cartItemBindingModel;
    }

    private BigDecimal calculateTotalPrice(UserCartBindingModel userCartBindingModel) {
        return userCartBindingModel.getCartItems().stream()
                .map(CartItemBindingModel::getTotalPriceOfProduct)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
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
