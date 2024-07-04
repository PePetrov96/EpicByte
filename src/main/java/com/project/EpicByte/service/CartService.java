package com.project.EpicByte.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    String showCartPage(String username, Model model, HttpSession session);
    String deleteItemFromUserCart (UUID id, String username, Model model, HttpSession session);
    ResponseEntity<String> addProductToCart(Map<String, Object> payload, Principal principal, HttpSession session);
}
