package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.ToyAddDTO;
import com.project.EpicByte.service.ToyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.project.EpicByte.util.Constants.ADMIN_PRODUCT_ADD_TOY_URL;
import static com.project.EpicByte.util.Constants.PRODUCT_DETAILS_URL;

@Controller
public class ToyController {
    private final ToyService toyService;

    @Autowired
    public ToyController(ToyService toyService) {
        this.toyService = toyService;
    }

    // Display detailed single Music entity page
    @GetMapping(PRODUCT_DETAILS_URL + "/toys/" + "{id}")
    public String viewToyProductDetails(@PathVariable Long id, Model model) {
        return this.toyService.displayDetailedViewToyPage(id,model);
    }

    @GetMapping("/products/toys")
    public String displayToysPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.toyService.displayAllToysPage(model, sort);
    }

    // Display Add Toy page
    @GetMapping(ADMIN_PRODUCT_ADD_TOY_URL)
    protected String displayProductAddToyPage(Model model) {
        return toyService.displayProductAddToyPage(model);
    }

    // Process Add new Toy
    @PostMapping(ADMIN_PRODUCT_ADD_TOY_URL)
    public String handleProductAddToyPage(@Valid @ModelAttribute("product") ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        return toyService.handleProductAddToy(toyAddDTO, bindingResult, model);
    }
}
