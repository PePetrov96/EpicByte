package com.project.EpicByte.web;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class UserOrderController {
    private final UserOrderService userOrderService;

    @Autowired
    public UserOrderController(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    @SlowExecutionWarning
    @GetMapping(USER_ORDERS_URL)
    public String displayUserOrdersPage(Model model, Principal principal) {
        return this.userOrderService.displayUserOrders(model, principal);
    }

    @SlowExecutionWarning
    @GetMapping(MODERATOR_ORDERS_URL)
    public String displayAdminOrdersPage(Model model) {
        return this.userOrderService.displayAdminAllUserOrders(model);
    }

    @SlowExecutionWarning
    @PostMapping(MODERATOR_ORDER_COMPLETE_URL + "{id}")
    public String completeOrder(@PathVariable UUID id, Model model) {
        return this.userOrderService.completeUserOrder(id);
    }

    @SlowExecutionWarning
    @GetMapping(ORDER_DETAILS_URL + "{id}")
    public String displayOrderDetails(@PathVariable UUID id, Model model) {
        return this.userOrderService.displayUserOrderDetails(id, model);
    }
}
