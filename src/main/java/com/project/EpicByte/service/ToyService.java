package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.ToyAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface ToyService {
    String displayProductAddToyPage(Model model);
    String handleProductAddToy(ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model);
}
