package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.BookAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface BookService {
    String displayProductAddBookPage(Model model);
    String handleProductAddBook(BookAddDTO bookAddDTO, BindingResult bindingResult, Model model);
    String displayAllBooksPage(Model model, String string);
    String displayDetailedViewBookPage(Long id, Model model);
}
