package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.OrderAddressDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;

public interface CartCheckoutService {
    String confirmCheckout(OrderAddressDTO orderAddressDTO, BindingResult bindingResult, Principal principal, Model model, HttpSession session);
    String showCartCheckoutPage(Principal principal, Model model, HttpSession session);
    String displayCartCheckoutConfirmationPage(Model model);
}
