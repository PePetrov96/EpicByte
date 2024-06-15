package com.project.EpicByte.web;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // LOGIN page display
    @GetMapping(LOGIN_URL)
    public String showLoginPage() {
        return userService.showLoginPage();
    }

    // LOGIN-ERROR page display
    @PostMapping(LOGIN_ERROR_URL)
    public String handleLoginError(Model model) {
        return userService.handleLoginError(model);
    }

    // REGISTER page display
    @GetMapping(REGISTER_URL)
    public String showRegisterPage(Model model) {
        return userService.showRegisterPage(model);
    }

    // REGISTER page handle update
    @PostMapping(REGISTER_URL)
    public String registerUser(@Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO, BindingResult bindingResult,
                               Model model) {
        return userService.registerUser(userRegisterDTO, model, bindingResult);
    }

    // USER PROFILE page display
    @GetMapping(USER_PROFILE_URL)
    public String showProfilePage(Model model, Principal principal) {
        return userService.showProfilePage(model, principal);
    }

    // USER PROFILE page handle update
    @PostMapping(USER_PROFILE_URL)
    public String updateProfilePage(@ModelAttribute("userUpdateDTO") @Valid UserUpdateDTO userUpdateDTO, BindingResult bindingResult,
                                    Model model, RedirectAttributes redirectAttributes, Principal principal) {
        return userService.updateProfilePage(userUpdateDTO, bindingResult, model, redirectAttributes, principal);
    }

    // USER ORDERS page display
    @GetMapping(USER_ORDERS_URL)
    public String showUserOrdersPage(Model model, Principal principal) {
        return userService.displayUserOrdersPage(model, principal);
    }
}
