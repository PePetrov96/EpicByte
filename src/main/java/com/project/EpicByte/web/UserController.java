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

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return userService.showLoginPage();
    }

    @PostMapping("/login-error")
    public String handleLoginError(Model model) {
        return userService.handleLoginError(model);
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        return userService.showRegisterPage(model);
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO, BindingResult bindingResult,
                               Model model) {
        return userService.registerUser(userRegisterDTO, model, bindingResult);
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        return userService.showProfilePage(model, principal);
    }

    @PostMapping("/profile")
    public String updateProfilePage(@ModelAttribute("userUpdateDTO") @Valid UserUpdateDTO userUpdateDTO,
                                    Model model, RedirectAttributes redirectAttributes, Principal principal) {
        return userService.updateProfilePage(userUpdateDTO, model, redirectAttributes, principal);
    }
}
