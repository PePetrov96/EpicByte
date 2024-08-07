package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.UUID;

public interface ToyService {
    String displayProductAddToyPage(Model model);
    String handleProductAddToy(ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model);
    String displayAllToysPage(Model model, String string);
    String displayDetailedViewToyPage(UUID id, Model model);
    String deleteToy(UUID id);
}
