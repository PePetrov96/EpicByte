package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.entity.UserOrder;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserOrderRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.UserOrderService;
import com.project.EpicByte.util.Breadcrumbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.*;

import static com.project.EpicByte.util.Constants.*;

@Service
public class UserOrderServiceImpl implements UserOrderService {
    private final UserOrderRepository userOrderRepository;
    private final UserRepository userRepository;
    private final Breadcrumbs breadcrumbs;

    @Autowired
    public UserOrderServiceImpl(UserOrderRepository userOrderRepository,
                                UserRepository userRepository,
                                Breadcrumbs breadcrumbs) {
        this.userOrderRepository = userOrderRepository;
        this.userRepository = userRepository;
        this.breadcrumbs = breadcrumbs;
    }

    @Override
    public String displayUserOrders(Model model, Principal principal) {
        UserEntity user = getUserEntityByUsername(principal);
        Set<UserOrder> userOrdersSet = this.userOrderRepository.findUserOrderByUserIdOrderByOrderDateDesc(user.getId());
        return returnModelPage(userOrdersSet, model);
    }

    @Override
    public String displayAdminAllUserOrders(Model model) {
        LinkedHashSet<UserOrder> userOrdersSet = new LinkedHashSet<>(userOrderRepository.findUserOrdersIncompleteOrderByOrderDateDesc());
        return returnModelPage(userOrdersSet, model);
    }

    @Override
    public String completeUserOrder(UUID id) {
        Optional<UserOrder> UserOrder = this.userOrderRepository.findUserOrderById(id);

        if (UserOrder.isPresent()) {
            UserOrder actualUserOrder = UserOrder.get();
            actualUserOrder.setComplete(true);
            userOrderRepository.save(actualUserOrder);
        }

        return "redirect:" + MODERATOR_ORDERS_URL;
    }

    @Override
    public String displayUserOrderDetails(UUID id, Model model) {
        Optional<UserOrder> UserOrder = this.userOrderRepository.findUserOrderById(id);

        if (UserOrder.isPresent()) {
            UserOrder actualUserOrder = UserOrder.get();
            model.addAttribute("orderItems", actualUserOrder.getOrderItems());
            model.addAttribute("totalCost", actualUserOrder.getTotalCost());
            breadcrumbs.addProductBreadcrumb(model, USER_ORDERS_URL, "Orders", String.valueOf(actualUserOrder.getId()));
            return ORDER_DETAILS_HTML;
        }

        return "redirect:" + INDEX_URL;
    }

    // Support methods

    // Set page model attributes
    private String returnModelPage(Set<UserOrder> userOrdersSet, Model model) {
        breadcrumbs.addProductBreadcrumb(model, USER_ORDERS_URL, "Orders");

        if (userOrdersSet.isEmpty()) {
            model.addAttribute("noUserOrders", true);
        } else {
            model.addAttribute("noUserOrders", false);
            model.addAttribute("userOrders", userOrdersSet);
        }

        return ORDERS_HTML;
    }

    // Get user. Transactional, since the collections are prone to errors
    protected UserEntity getUserEntityByUsername(Principal principal) {
        UserEntity user = this.userRepository
                .findUserByUsernameWithInitializedOrders(principal.getName());

        if (user == null) {
            throw new UsernameNotFoundException(principal.getName());
        }

        return user;
    }
}
