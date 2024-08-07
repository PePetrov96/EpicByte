package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.UUID;

public interface TextbookService {
    String displayProductAddTextbookPage(Model model);
    String handleProductAddTextbook(TextbookAddDTO textbookAddDTO, BindingResult bindingResult, Model model);
    String displayAllTextbooksPage(Model model, String string);
    String displayDetailedViewTextbookPage(UUID id, Model model);
    String deleteTextbook(UUID id);
}
