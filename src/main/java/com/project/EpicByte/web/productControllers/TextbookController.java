package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.TextbookAddDTO;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.service.TextbookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.project.EpicByte.util.Constants.ADMIN_PRODUCT_ADD_TEXTBOOK_URL;
import static com.project.EpicByte.util.Constants.PRODUCT_DETAILS_URL;

@Controller
public class TextbookController {
    private final TextbookService textbookService;

    @Autowired
    public TextbookController(TextbookService textbookService) {
        this.textbookService = textbookService;
    }

    // Display detailed single Textbook entity page
    @GetMapping(PRODUCT_DETAILS_URL + "/textbooks/" + "{id}")
    public String viewTextbookProductDetails(@PathVariable Long id, Model model) {
        return textbookService.displayDetailedViewTextbookPage(id, model);
    }

    // Display all Textbooks page
    @GetMapping("/products/textbooks")
    public String displayTextbooksPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.textbookService.displayAllTextbooksPage(model, sort);
    }

    // Display Add Textbook page
    @GetMapping(ADMIN_PRODUCT_ADD_TEXTBOOK_URL)
    public String displayProductAddTextbookPage(Model model) {
        return textbookService.displayProductAddTextbookPage(model);
    }

    // Process Add new Textbook
    @PostMapping(ADMIN_PRODUCT_ADD_TEXTBOOK_URL)
    public String handleProductAddTextbookPage(@Valid @ModelAttribute("product") TextbookAddDTO textbookAddDTODTO,
                                               BindingResult bindingResult, Model model) {
        return textbookService.handleProductAddTextbook(textbookAddDTODTO, bindingResult, model);
    }
}
