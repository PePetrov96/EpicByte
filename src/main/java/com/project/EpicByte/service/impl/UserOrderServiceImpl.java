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
public class UserOrderServiceImpl extends Breadcrumbs implements UserOrderService {
    private final UserOrderRepository userOrderRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserOrderServiceImpl(UserOrderRepository userOrderRepository, UserRepository userRepository) {
        this.userOrderRepository = userOrderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String displayUserOrders(Model model, Principal principal) {
        try {
            UserEntity user = getUserEntityByUsername(principal.getName());
            Set<UserOrder> userOrdersSet = this.userOrderRepository.findUserOrderByUserId(user.getId());
            return returnModelPage(userOrdersSet, model);
        } catch (NullPointerException | UsernameNotFoundException exception) {
            return returnErrorPage(model);
        }
    }

    @Override
    public String displayAdminAllUserOrders(Model model) {
        try {
            LinkedHashSet<UserOrder> userOrdersSet = new LinkedHashSet<>(userOrderRepository.findUserOrdersComplete());
            return returnModelPage(userOrdersSet, model);
        } catch (NullPointerException | UsernameNotFoundException exception) {
            return returnErrorPage(model);
        }
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
            addProductBreadcrumb(model, USER_ORDERS_URL, "Orders", String.valueOf(actualUserOrder.getId()));
            return ORDER_DETAILS_HTML;
        }

        return "redirect:" + INDEX_URL;
    }

    // Support methods

    // Set page model attributes
    private String returnModelPage(Set<UserOrder> userOrdersSet, Model model) {
        addProductBreadcrumb(model, USER_ORDERS_URL, "Orders");

        if (userOrdersSet.isEmpty()) {
            model.addAttribute("noUserOrders", true);
        } else {
            model.addAttribute("noUserOrders", false);
            model.addAttribute("userOrders", userOrdersSet);
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
    protected UserEntity getUserEntityByUsername(String username) {
        UserEntity user = this.userRepository
                .findUserByUsernameWithInitializedOrders(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
