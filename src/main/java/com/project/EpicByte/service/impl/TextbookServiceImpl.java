package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.TextbookAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.repository.TextbookRepository;
import com.project.EpicByte.service.TextbookService;
import com.project.EpicByte.util.Breadcrumbs;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.project.EpicByte.util.Constants.*;

@Service
public class TextbookServiceImpl extends Breadcrumbs implements TextbookService {
    private final TextbookRepository textbookRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Autowired
    public TextbookServiceImpl(TextbookRepository textbookRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.textbookRepository = textbookRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    public String displayProductAddTextbookPage(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("textbook.text", null, locale);

        model.addAttribute("linkType", "textbook");
        model.addAttribute("productType", productTypeMessage);

        model.addAttribute("product", new TextbookAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("textbook"));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddTextbook(TextbookAddDTO textbookAddDTO, BindingResult bindingResult, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("textbook.text", null, locale);

        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "textbook");

        if (bindingResult.hasErrors()) {
            model.addAttribute("linkType", "textbook");
            model.addAttribute("fieldsMap", getFieldNames("textbook"));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addTextbookToDatabase(textbookAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Textbook added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllTextbooksPage(Model model, String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("textbooks.text", null, locale);

        addProductBreadcrumb(model, null, productTypeMessage);
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "textbooks");

        List<Textbook> textbookList;

        if (sort == null) {
            textbookList = this.textbookRepository.findAll();
        } else if (sort.equals("lowest")) {
            textbookList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            textbookList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            textbookList = getAllSortedAlphabetically();
        } else {
            textbookList = this.textbookRepository.findAll();
        }

        model.addAttribute("productList", textbookList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewTextbookPage(Long id, Model model) {
        Textbook textbook = textbookRepository.findTextbookById(id);

        addProductBreadcrumb(model, null, "Textbook", textbook.getProductName());
        model.addAttribute("product", textbook);
        model.addAttribute("productDetails", null);

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
    private List<Textbook> getAllSortedByLowestPrice() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Textbook> getAllSortedByHighestPrice() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Textbook> getAllSortedAlphabetically() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }

    private void addTextbookToDatabase(TextbookAddDTO textbookAddDTO) {
        Textbook textbook = modelMapper.map(textbookAddDTO, Textbook.class);

        textbook.setProductType(ProductTypeEnum.TEXTBOOK);
        textbook.setDateCreated(LocalDate.now());
        textbook.setNewProduct(true);

        textbookRepository.saveAndFlush(textbook);

    }
}
