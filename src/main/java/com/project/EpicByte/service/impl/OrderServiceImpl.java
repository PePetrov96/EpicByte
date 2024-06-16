package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.entity.Order;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.OrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.OrderService;
import com.project.EpicByte.util.Breadcrumbs;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Transactional
@Service
public class OrderServiceImpl extends Breadcrumbs implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String displayUserOrders(Model model, Principal principal) {
        try {
            UserEntity user = getUserEntityByUsername(principal.getName());
            List<Order> orderList = user.getOrders();
            return returnModelPage(orderList, model);
        } catch (NullPointerException | UsernameNotFoundException exception) {
            return returnErrorPage(model);
        }
    }
    @Override
    public String displayAdminAllOrders(Model model) {
        try {
            List<Order> orderList = orderRepository.findAll();
            return returnModelPage(orderList, model);
        } catch (NullPointerException | UsernameNotFoundException exception) {
            return returnErrorPage(model);
        }
    }

    @Transactional
    @Override
    public String completeOrder(UUID id) {
        Optional<Order> order = this.orderRepository.findOrderById(id);

        if (order.isPresent()) {
            Order actualOrder = order.get();
            actualOrder.setComplete(true);
            orderRepository.save(actualOrder);
        }

        return "redirect:" + MODERATOR_ORDERS_URL;
    }

    @Override
    @Transactional
    public String displayOrderDetails(UUID id, Model model) {
        Optional<Order> order = this.orderRepository.findOrderById(id);

        if (order.isPresent()) {
            Order actualOrder = order.get();

            Hibernate.initialize(actualOrder.getOrderItems());

            model.addAttribute("orderItems", actualOrder.getOrderItems());
            model.addAttribute("totalCost", actualOrder.getTotalCost());
            addProductBreadcrumb(model, USER_ORDERS_URL, "Orders", String.valueOf(actualOrder.getId()));
            return ORDER_DETAILS_HTML;
        }

        return INDEX_HTML;
    }

    // Support methods

    // Set page model attributes
    private String returnModelPage(List<Order> orderList, Model model) {
        addProductBreadcrumb(model, USER_ORDERS_URL, "Orders");

        if (orderList.isEmpty()) {
            model.addAttribute("noOrders", true);
        } else {
            model.addAttribute("noOrders", false);
            model.addAttribute("userOrders", orderList);
        }

        return ORDERS_HTML;
    }

    // Return error page
    private String returnErrorPage(Model model) {
        model.addAttribute("errorType", "Oops...");
        model.addAttribute("errorText", "Something went wrong!");
        return ERROR_PAGE_HTML;
    }

    // Get user. Transactional, since the collections are prone to errors
    @Transactional
    protected UserEntity getUserEntityByUsername(String username) {
        UserEntity user = this.userRepository
                .findUserEntityByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Hibernate.initialize(user.getCartItems());
        Hibernate.initialize(user.getOrders());

        return user;
    }
}
