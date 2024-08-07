package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.BookAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.UUID;

public interface BookService {
    String displayProductAddBookPage(Model model);
    String handleProductAddBook(BookAddDTO bookAddDTO, BindingResult bindingResult, Model model);
    String displayAllBooksPage(Model model, String string);
    String displayDetailedViewBookPage(UUID id, Model model);
    String deleteBook(UUID id);
}
