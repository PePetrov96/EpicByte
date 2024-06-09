package com.project.EpicByte.web;

import com.project.EpicByte.model.dto.BookAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageDisplayController extends Breadcrumbs {
    @GetMapping("/")
    public String displayHomePage() {
        return "index";
    }

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

    @GetMapping("/products/toys")
    public String displayToysPage(Model model) {
        addProductBreadcrumb(model, null, "Toys");
        model.addAttribute("productType", "Toys");
        return "products-all";
    }

    @GetMapping("/product/details")
    public String displayProductDetailsPage(Model model) {
        addProductBreadcrumb(model, null, "Books", "Book Name");
        return "product-details";
    }

    @GetMapping("/admin/product/update")
    public String displayProductUpdatePage(Model model) {
        return "product-update";
    }


//    @GetMapping("/admin/product/add/textbook")
//    public String displayProductAddTextbookPage(Model model) {
//        return "product-add";
//    }
//
//    @GetMapping("/admin/product/add/music")
//    public String displayProductAddMusicPage(Model model) {
//        return "product-add";
//    }
//
//    @GetMapping("/admin/product/add/movie")
//    public String displayProductAddMoviePage(Model model) {
//        return "product-add";
//    }
//
//    @GetMapping("/admin/product/add/toy")
//    public String displayProductAddToyPage(Model model) {
//        return "product-add";
//    }

    @GetMapping("/users/cart")
    public String displayCartPage(Model model) {
        addProductBreadcrumb(model, "/users/cart", "Cart");
        return "cart";
    }

    @GetMapping("/terms-and-conditions")
    public String displayTermsAndConditionsPage(Model model) {
        addProductBreadcrumb(model, "/terms-and-conditions", "Terms and Conditions");
        model.addAttribute("pageType", "Terms and Conditions");
        return "display-text";
    }

    @GetMapping("/privacy")
    public String displayPrivacyPage(Model model) {
        addProductBreadcrumb(model, "/privacy", "Privacy");
        model.addAttribute("pageType", "Privacy");
        return "display-text";
    }

    @GetMapping("/users/cart/checkout/confirm")
    public String displayConfirmCheckoutPage(Model model) {
        addProductBreadcrumb(model, null, "Cart", "Checkout", "Confirm Checkout");
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Your order has been received!");
        return "display-text";
    }

    @GetMapping("/users/orders")
    public String displayUserOrdersPage(Model model) {
        addProductBreadcrumb(model, "/users/orders", "My orders");
        model.addAttribute("ordersType", "My orders");
        return "orders";
    }

    @GetMapping("/admin/orders")
    public String displayAdminOrdersPage(Model model) {
        addProductBreadcrumb(model, "/admin/orders", "All orders");
        model.addAttribute("ordersType", "All orders");
        return "orders";
    }
}