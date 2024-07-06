package com.project.EpicByte.service.impl;

import com.project.EpicByte.exceptions.EmptyCartException;
import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserOrder;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.OrderItem;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.UserOrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.CartCheckoutService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.Breadcrumbs2;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import static com.project.EpicByte.util.Constants.*;

@Service
public class CartCheckoutServiceImpl implements CartCheckoutService {
    private final UserOrderRepository userOrderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    private final Breadcrumbs2 breadcrumbs;

    @Autowired
    public CartCheckoutServiceImpl(UserOrderRepository userOrderRepository,
                                   CartRepository cartRepository,
                                   UserRepository userRepository,
                                   MessageSource messageSource,
                                   ModelMapper modelMapper,
                                   Breadcrumbs2 breadcrumbs) {
        this.userOrderRepository = userOrderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
        this.modelMapper = modelMapper;
        this.breadcrumbs = breadcrumbs;
    }

    @Override
    public String showCartCheckoutPage(Principal principal, Model model, HttpSession session) {
        try {
            model.addAttribute("cartItemList", session.getAttribute("userCartBindingModel"));
            model.addAttribute("orderAddressDTO", new OrderAddressDTO());
            breadcrumbs.addProductBreadcrumb(model, USER_CART_URL, "Cart", "Checkout");
            return CART_CHECKOUT_HTML;
        }catch (EmptyCartException e) {
            return returnEmptyCartPage(model);
        }
    }

    @Override
    @Transactional
    public String confirmCheckout(OrderAddressDTO orderAddressDTO,
                                  BindingResult bindingResult,
                                  Principal principal,
                                  Model model,
                                  HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItemList", session.getAttribute("userCartBindingModel"));
            breadcrumbs.addProductBreadcrumb(model, USER_CART_URL, "Cart", "Checkout");
            return CART_CHECKOUT_HTML;
        }

        try {
            createUserOrder(principal.getName(), orderAddressDTO);
            model.addAttribute("pageType", "Completed Successfully");
            model.addAttribute("pageText", getLocalizedText("order.successfully.received.text"));
            session.setAttribute("numItems", 0);
            return DISPLAY_TEXT_HTML;
        } catch (EmptyCartException exception) {
            return returnEmptyCartPage(model);
        }
    }

    @Override
    public String displayCartCheckoutConfirmationPage(Model model) {
        breadcrumbs.addProductBreadcrumb(model, "/user/cart", "Cart", "Confirm Checkout");
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("order.successfully.received.text"));
        return DISPLAY_TEXT_HTML;
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
            OrderItem orderItem = modelMapper.map(product, OrderItem.class);

            int quantity = productCountMap.get(product);
            orderItem.setQuantity(quantity);
            orderItem.setTotalProductPrice(product.getProductPrice().multiply(new BigDecimal(quantity)));
            orderItem.setUserOrder(userOrder);

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    protected void finalizeOrderCreation(UserOrder userOrder, UserEntity userEntity) {
        this.userOrderRepository.save(userOrder);

        UserEntity user = this.userRepository
                .findUserByUsernameWithInitializedCartItems(userEntity.getUsername());

        user.getCartItems().clear();

        this.userRepository.saveAndFlush(user);
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
        order.setComplete(false); // default orders are "not shipped"
        order.setOrderDate(LocalDate.now()); // all orders are created for current local time
        order.setTotalCost(new BigDecimal("4.99")); // by default +4.99 for delivery
        return order;
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private UserEntity getUserEntityByUsername(String username) {
        UserEntity user = this.userRepository
                .findUserEntityByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    private String returnEmptyCartPage(Model model) {
        model.addAttribute("emptyCart", true);
        breadcrumbs.addProductBreadcrumb(model, USER_CART_URL, "Cart");
        return CART_HTML;
    }
}
