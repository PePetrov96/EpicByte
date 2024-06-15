package com.project.EpicByte.web;

import com.project.EpicByte.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.ADMIN_ORDERS_URL;
import static com.project.EpicByte.util.Constants.USER_ORDERS_URL;

@Controller
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(USER_ORDERS_URL)
    public String displayUserOrdersPage(Model model, Principal principal) {
        return this.orderService.displayUserOrders(model, principal);
    }

    @GetMapping(ADMIN_ORDERS_URL)
    public String displayAdminOrdersPage(Model model) {
        return this.orderService.displayAdminAllOrders(model);
    }

    @PostMapping("/admin/order/complete/{id}")
    public String completeOrder(@PathVariable UUID id, Model model) {
        return this.orderService.completeOrder(id);
    }

    @GetMapping("/order/details/{id}")
    public String displayOrderDetails(@PathVariable UUID id, Model model) {
        return this.orderService.displayOrderDetails(id, model);
    }
}
