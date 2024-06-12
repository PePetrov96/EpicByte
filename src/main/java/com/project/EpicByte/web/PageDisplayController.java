package com.project.EpicByte.web;

import com.project.EpicByte.service.*;
import com.project.EpicByte.util.Breadcrumbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageDisplayController extends Breadcrumbs {
    private final BookService bookService;
    private final TextbookService textbookService;
    private final MusicService musicService;
    private final MovieService movieService;
    private final ToyService toyService;

    @Autowired
    public PageDisplayController(BookService bookService, TextbookService textbookService, MusicService musicService, MovieService movieService, ToyService toyService) {
        this.bookService = bookService;
        this.textbookService = textbookService;
        this.musicService = musicService;
        this.movieService = movieService;
        this.toyService = toyService;
    }

    @GetMapping("/")
    public String displayHomePage() {
        return "index";
    }

    @GetMapping("/admin/product/update")
    public String displayProductUpdatePage(Model model) {
        return "product-update";
    }

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