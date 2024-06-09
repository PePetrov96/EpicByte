package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.BookAddDTO;
import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.model.entity.productEntities.*;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.repository.BookRepository;
import com.project.EpicByte.service.BookService;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.project.EpicByte.util.Constants.*;

@Service
public class BookServiceImpl extends FieldNamesGenerator implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public String displayProductAddBookPage(Model model) {
        model.addAttribute("productType", "book");
        model.addAttribute("product", new BookAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("book"));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddBook(BookAddDTO bookAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", "book");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("book"));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addBookToDatabase(bookAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Book added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    // Support methods
    private void addBookToDatabase(BookAddDTO bookAddDTO) {
        Book book = modelMapper.map(bookAddDTO, Book.class);

        book.setProductType(ProductTypeEnum.BOOK);
        bookRepository.saveAndFlush(book);

    }
}
