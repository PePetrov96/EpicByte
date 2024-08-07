package com.project.EpicByte.web;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.service.CartCheckoutService;
import com.project.EpicByte.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.Map;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class CartController {
    private final CartService cartService;
    private final CartCheckoutService cartCheckoutService;

    @Autowired
    public CartController(CartService cartService,
                          CartCheckoutService cartCheckoutService) {
        this.cartService = cartService;
        this.cartCheckoutService = cartCheckoutService;
    }

    // Display cart page
    @SlowExecutionWarning
    @GetMapping(USER_CART_URL)
    public String displayCartPage(Principal principal,
                                  Model model,
                                  HttpSession session) {
        return this.cartService.showCartPage(principal.getName(), model, session);
    }

    // Delete items from cart
    @SlowExecutionWarning
    @PostMapping(USER_CART_DELETE_URL + "{id}")
    public String handleItemDeletionFromCart(@PathVariable UUID id,
                                             Principal principal,
                                             Model model,
                                             HttpSession session) {
        return this.cartService.deleteItemFromUserCart(id, principal.getName(), model, session);
    }

    // Add product to cart (no redirects. through AJAX request)
    @SlowExecutionWarning
    @PostMapping(value = PRODUCT_ADD_TO_CART_URL, consumes = "application/json")
    public @ResponseBody ResponseEntity<?> addItemToUserCart(@RequestBody Map<String, Object> payload,
                                                             Principal principal,
                                                             HttpSession session) {
        return this.cartService.addProductToCart(payload, principal, session);
    }

    // Display cart-checkout page
    @SlowExecutionWarning
    @GetMapping(USERS_CART_CHECKOUT_URL)
    public String displayCartCheckoutPage(Principal principal,
                                          Model model,
                                          HttpSession session) {
        return this.cartCheckoutService.showCartCheckoutPage(principal, model, session);
    }

    // Process cart-checkout page
    @SlowExecutionWarning
    @PostMapping(USERS_CART_CHECKOUT_URL)
    public String handleCartCheckoutConfirmation(@Valid @ModelAttribute("orderAddressDTO") OrderAddressDTO orderAddressDTO,
                                                 BindingResult bindingResult,
                                                 Principal principal,
                                                 Model model,
                                                 HttpSession session) {
        return this.cartCheckoutService.confirmCheckout(orderAddressDTO, bindingResult, principal, model, session);
    }

    @SlowExecutionWarning
    @GetMapping(USERS_CART_CHECKOUT_CONFIRM_URL)
    public String displayConfirmCheckoutPage(Model model) {
        return this.cartCheckoutService.displayCartCheckoutConfirmationPage(model);
    }
}
