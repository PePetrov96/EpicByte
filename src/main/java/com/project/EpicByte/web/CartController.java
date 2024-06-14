package com.project.EpicByte.web;

import com.project.EpicByte.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.UUID;

@Controller
public class CartController {
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/cart")
    public String displayCartPage(Principal principal, Model model) {
        return this.cartService.showCartPage(principal.getName(), model);
    }

    @PostMapping("/user/cart/delete/{id}")
    public String handleItemDeletionFromCart(@PathVariable UUID id, Principal principal, Model model) {
        return this.cartService.deleteItemFromUserCart(id, principal.getName(), model);
    }

    @PostMapping("/product/add-to-cart")
    public String addToCart(@RequestParam UUID productId, @RequestParam String productType, Principal principal, Model model) {
        return this.cartService.addToCart(productId, productType, principal.getName(), model);
    }
}
