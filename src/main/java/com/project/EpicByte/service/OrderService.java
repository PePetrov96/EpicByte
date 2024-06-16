package com.project.EpicByte.service;

import org.springframework.ui.Model;

import java.security.Principal;
import java.util.UUID;

public interface OrderService {
    String displayUserOrders(Model model, Principal principal);
    String displayAdminAllOrders(Model model);
    String completeOrder (UUID id);
    String displayOrderDetails (UUID id, Model model);
}
