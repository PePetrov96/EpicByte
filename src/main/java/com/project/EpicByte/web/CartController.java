package com.project.EpicByte.web;

import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import com.project.EpicByte.service.CartService;
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

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Display cart page
    @GetMapping(USER_CART_URL)
    public String displayCartPage(Principal principal, Model model) {
        return this.cartService.showCartPage(principal, model);
    }

    // Delete items from cart
    @PostMapping(USER_CART_DELETE_URL)
    public String handleItemDeletionFromCart(@PathVariable UUID id, Principal principal, Model model) {
        return this.cartService.deleteItemFromUserCart(id, principal.getName(), model);
    }

    // Add product to cart (no redirects)
    @PostMapping(value = PRODUCT_ADD_TO_CART_URL, consumes = "application/json")
    public @ResponseBody ResponseEntity<?> addItemToUserCart(@RequestBody Map<String, Object> payload,  Principal principal) {
        return this.cartService.addProductToCart(payload, principal);
    }

    // Display cart-checkout page
    @GetMapping(USERS_CART_CHECKOUT_URL)
    public String displayCartCheckoutPage(Principal principal, Model model) {
        return this.cartService.showCartCheckoutPage(principal, model);
    }

    // Process cart-checkout page
    @PostMapping(USERS_CART_CHECKOUT_URL)
    public String handleCartCheckoutConfirmation(@Valid @ModelAttribute("orderAddressDTO") OrderAddressDTO orderAddressDTO,
                                                 BindingResult bindingResult, Principal principal, Model model) {
        return this.cartService.confirmCheckout(orderAddressDTO, bindingResult, principal, model);
    }
}
