package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.TextbookAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.repository.TextbookRepository;
import com.project.EpicByte.service.TextbookService;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.project.EpicByte.util.Constants.*;

@Service
public class TextbookServiceImpl extends FieldNamesGenerator implements TextbookService {
    private final TextbookRepository textbookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TextbookServiceImpl(TextbookRepository textbookRepository, ModelMapper modelMapper) {
        this.textbookRepository = textbookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String displayProductAddTextbookPage(Model model) {
        model.addAttribute("productType", "textbook");
        model.addAttribute("product", new TextbookAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("textbook"));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddTextbook(TextbookAddDTO textbookAddDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productType", "textbook");
            model.addAttribute("product", textbookAddDTO);
            model.addAttribute("fieldsMap", getFieldNames("textbook"));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addTextbookToDatabase(textbookAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Textbook added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    // Support methods
    private void addTextbookToDatabase(TextbookAddDTO textbookAddDTO) {
        Textbook textbook = modelMapper.map(textbookAddDTO, Textbook.class);
        textbook.setProductType(ProductTypeEnum.TEXTBOOK);
        textbookRepository.saveAndFlush(textbook);

    }
}
