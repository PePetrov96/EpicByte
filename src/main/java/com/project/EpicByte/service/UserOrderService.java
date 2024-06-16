package com.project.EpicByte.service;

import org.springframework.ui.Model;

import java.security.Principal;
import java.util.UUID;

public interface UserOrderService {
    String displayUserOrders(Model model, Principal principal);
    String displayAdminAllUserOrders(Model model);
    String completeUserOrder (UUID id);
    String displayUserOrderDetails (UUID id, Model model);
}
