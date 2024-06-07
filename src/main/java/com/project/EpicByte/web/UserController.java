package com.project.EpicByte.web;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import com.project.EpicByte.model.dto.UserUpdateDTO;
import com.project.EpicByte.service.UserService;
import com.project.EpicByte.validation.UsernameAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //LOGIN logic
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth-login";
    }

    @PostMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "auth-login";
    }

    //REGISTER logic
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "auth-register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") UserRegisterDTO userRegisterDTO,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth-register";
        }

        try {
            this.userService.registerUser(userRegisterDTO);
            return "redirect:/users/login";
        } catch (UsernameAlreadyExistsException e) {
            bindingResult.rejectValue("floatingUsername", "error.userRegisterDTO", e.getMessage());
            return "auth-register";
        }
    }

    // USER PROFILE
    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        addProductBreadcrumb(model, "/users/profile", "User Profile");

        // set user values
        UserUpdateDTO userUpdateDTO = this.userService.getUserUpdateDTOByUsername(principal.getName());
        model.addAttribute("userUpdateDTO", userUpdateDTO);

        // check if it is a redirect with successful operation or a new request
        if (model.containsAttribute("operationSuccess")) {
            model.addAttribute("operationSuccess", true);
        } else {
            model.addAttribute("operationSuccess", false);
        }

        return "user-profile";
    }

    @PostMapping("/profile")
    public String updateProfilePage(@ModelAttribute("userUpdateDTO") @Valid UserUpdateDTO userUpdateDTO,
                                    BindingResult bindingResult,
                                    Model model, RedirectAttributes redirectAttributes) {

        // check for errors and ask the user to try again
        if (bindingResult.hasErrors()) {
            addProductBreadcrumb(model, "/users/profile", "User Profile");
            return "user-profile";
        }

        // save information, set the "true" for the success message and redirect with updated values
        this.userService.updateUser(userUpdateDTO);
        redirectAttributes.addFlashAttribute("operationSuccess", true);
        return "redirect:/users/profile";
    }

    // USER ORDERS

    // SUPPORT METHOD
    private void addProductBreadcrumb(Model model, String pageUrl, String... pageNames) {
        Map<String, String> breadcrumbs = new LinkedHashMap<>();
        breadcrumbs.put("Home", "/");

        if (pageUrl == null) {
            for (int i = 0; i < pageNames.length; i++) {
                if (i == 0) {
                    breadcrumbs.put(pageNames[i], "/products/" + pageNames[i].replace(" ", "").toLowerCase());
                } else {
                    breadcrumbs.put(pageNames[i], "/" + pageNames[i].replace(" ", "").toLowerCase());
                }
            }
        } else {
            breadcrumbs.put(pageNames[0], pageUrl);
        }

        model.addAttribute("breadcrumbs", breadcrumbs);
    }
}
