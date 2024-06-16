package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    String showCartPage(String username, Model model);
    String deleteItemFromUserCart (UUID id, String username, Model model);
    ResponseEntity<String> addProductToCart(Map<String, Object> payload, Principal principal);
    String showCartCheckoutPage(Principal principal, Model model);
    String confirmCheckout(OrderAddressDTO orderAddressDTO, BindingResult bindingResult, Principal principal, Model model);
}
