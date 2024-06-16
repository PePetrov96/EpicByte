package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.service.productServices.ToyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class ToyController {
    private final ToyService toyService;

    @Autowired
    public ToyController(ToyService toyService) {
        this.toyService = toyService;
    }

    // Display detailed single Music entity page
    @GetMapping("/toy" + PRODUCT_DETAILS_URL + "/{id}")
    public String viewToyProductDetails(@PathVariable UUID id, Model model) {
        return this.toyService.displayDetailedViewToyPage(id,model);
    }

    @GetMapping(ALL_TOYS_URL)
    public String displayToysPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.toyService.displayAllToysPage(model, sort);
    }

    // Display Add Toy page
    @GetMapping(MODERATOR_PRODUCT_ADD_TOY_URL)
    protected String displayProductAddToyPage(Model model) {
        return toyService.displayProductAddToyPage(model);
    }

    // Process Add new Toy
    @PostMapping(MODERATOR_PRODUCT_ADD_TOY_URL)
    public String handleProductAddToyPage(@Valid @ModelAttribute("product") ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        return toyService.handleProductAddToy(toyAddDTO, bindingResult, model);
    }
}
