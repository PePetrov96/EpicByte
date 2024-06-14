package com.project.EpicByte.service;

import org.springframework.ui.Model;

import java.util.UUID;

public interface CartService {
    String addToCart(UUID id, String productType, String username, Model model);
    String showCartPage(String username, Model model);
    String deleteItemFromUserCart (UUID id, String username, Model model);
}
