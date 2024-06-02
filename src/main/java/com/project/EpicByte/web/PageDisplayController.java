package com.project.EpicByte.web;

import com.project.EpicByte.model.entity.User;
import com.project.EpicByte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class PageDisplayController {
    private final UserRepository userRepository;

    @Autowired
    public PageDisplayController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String displayHomePage() {
        User user = new User();
        user.setUsername("Final test success");
        this.userRepository.save(user);
        return "index";
    }

    @GetMapping("/user/register")
    public String displayRegisterPage() {
        return "auth-register";
    }

    @GetMapping("/user/login")
    public String displayLoginPage() {
        return "auth-login";
    }

    // START (to be migrated into 1 class, by passing product Main type)
    @GetMapping("/products/books")
    public String displayBooksPage(Model model) {
        addProductBreadcrumb(model, null, "Books");
        model.addAttribute("productType", "Books");
        return "products-all";
    }

    @GetMapping("/products/textbooks")
    public String displayTextbooksPage(Model model) {
        addProductBreadcrumb(model, null, "Textbooks");
        model.addAttribute("productType", "Textbooks");
        return "products-all";
    }


    @GetMapping("/products/music")
    public String displayMusicPage(Model model) {
        addProductBreadcrumb(model, null, "Music");
        model.addAttribute("productType", "Music");
        return "products-all";
    }

    @GetMapping("/products/movies")
    public String displayMoviePage(Model model) {
        addProductBreadcrumb(model, null, "Movies");
        model.addAttribute("productType", "Movies");
        return "products-all";
    }


    @GetMapping("/products/games-and-toys")
    public String displayGamesAndToysPage(Model model) {
        addProductBreadcrumb(model, null, "Games and Toys");
        model.addAttribute("productType", "Games and Toys");
        return "products-all";
    }
    // END

    @GetMapping("/product/details")
    public String displayProductDetailsPage(Model model) {
        addProductBreadcrumb(model, null, "Books", "Book Name");
        return "product-details";
    }

    @GetMapping("/product/update")
    public String displayProductUpdatePage(Model model) {
        return "product-update";
    }

    @GetMapping("/admin/product/add")
    public String displayProductAddPage(Model model) {
        return "product-add";
    }

    @GetMapping("/cart")
    public String displayCartPage(Model model) {
        addProductBreadcrumb(model, "/cart", "Cart");
        return "cart";
    }

    @GetMapping("/tandc")
    public String displayTermsAndConditionsPage(Model model) {
        addProductBreadcrumb(model, "/tandc", "Terms and Conditions");
        model.addAttribute("pageType", "Terms and Conditions");
        return "display-text";
    }

    @GetMapping("/privacy")
    public String displayPrivacyPage(Model model) {
        addProductBreadcrumb(model, "/privacy", "Privacy");
        model.addAttribute("pageType", "Privacy");
        return "display-text";
    }

    @GetMapping("/cart/checkout/confirm")
    public String displayConfirmCheckoutPage(Model model) {
        addProductBreadcrumb(model, null, "Cart", "Checkout", "Confirm Checkout");
        model.addAttribute("pageType", "Checkout");
        return "display-text";
    }

    @GetMapping("/user/orders")
    public String displayUserOrdersPage(Model model) {
        addProductBreadcrumb(model, "/user/orders", "My orders");
        model.addAttribute("ordersType", "My orders");
        return "orders";
    }

    @GetMapping("/admin/orders")
    public String displayAdminOrdersPage(Model model) {
        addProductBreadcrumb(model, "/admin/orders", "All orders");
        model.addAttribute("ordersType", "All orders");
        return "orders";
    }

    @GetMapping("/user/profile")
    public String displayUserProfilePage(Model model) {
        addProductBreadcrumb(model, "/user/profile", "User Profile");
        return "user-profile";
    }

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