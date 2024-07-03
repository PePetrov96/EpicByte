package com.project.EpicByte.web.productControllers;

import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
import com.project.EpicByte.service.product.TextbookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;

@Controller
public class TextbookController {
    private final TextbookService textbookService;

    @Autowired
    public TextbookController(TextbookService textbookService) {
        this.textbookService = textbookService;
    }

    // Display detailed single Textbook entity page
    @GetMapping("/textbook" + PRODUCT_DETAILS_URL + "/{id}")
    public String viewTextbookProductDetails(@PathVariable UUID id, Model model) {
        return textbookService.displayDetailedViewTextbookPage(id, model);
    }

    // Display all Textbooks page
    @GetMapping(ALL_TEXTBOOKS_URL)
    public String displayTextbooksPage(Model model, @RequestParam(name = "sort", required = false) String sort) {
        return this.textbookService.displayAllTextbooksPage(model, sort);
    }

    // Display Add Textbook page
    @GetMapping(MODERATOR_PRODUCT_ADD_TEXTBOOK_URL)
    public String displayProductAddTextbookPage(Model model) {
        return textbookService.displayProductAddTextbookPage(model);
    }

    // Process Add new Textbook
    @PostMapping(MODERATOR_PRODUCT_ADD_TEXTBOOK_URL)
    public String handleProductAddTextbookPage(@Valid @ModelAttribute("product") TextbookAddDTO textbookAddDTODTO,
                                               BindingResult bindingResult, Model model) {
        return textbookService.handleProductAddTextbook(textbookAddDTODTO, bindingResult, model);
    }

    // Delete a textbook
    @GetMapping(MODERATOR_TEXTBOOKS_DELETE_URL)
    public String deleteTextbooks(@PathVariable UUID id) {
        return this.textbookService.deleteTextbook(id);
    }
}
