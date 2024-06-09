package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.TextbookAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface TextbookService {
    String displayProductAddTextbookPage(Model model);
    String handleProductAddTextbook(TextbookAddDTO textbookAddDTO, BindingResult bindingResult, Model model);
}
