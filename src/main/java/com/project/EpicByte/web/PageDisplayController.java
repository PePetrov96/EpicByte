package com.project.EpicByte.web;

import com.project.EpicByte.util.Breadcrumbs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class PageDisplayController extends Breadcrumbs {
    @GetMapping(INDEX_URL)
    public String displayHomePage() {
        return INDEX_HTML;
    }

    @GetMapping(ADMIN_PRODUCT_UPDATE_URL)
    public String displayProductUpdatePage(Model model) {
        return PRODUCT_UPDATE_HTML;
    }
//
//    @GetMapping(USER_CART_URL)
//    public String displayCartPage(Model model) {
//        addProductBreadcrumb(model, USER_CART_URL, "Cart");
//        return CART_HTML;
//    }


    @GetMapping(ADMIN_ORDERS_URL)
    public String displayAdminOrdersPage(Model model) {
//        addProductBreadcrumb(model, "/admin/orders", "All orders");
        model.addAttribute("ordersType", "All orders");
        return ORDERS_HTML;
    }
}