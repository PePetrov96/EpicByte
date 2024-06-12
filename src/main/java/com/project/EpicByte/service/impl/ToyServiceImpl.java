package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.ToyAddDTO;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.ToyRepository;
import com.project.EpicByte.service.ToyService;
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
public class ToyServiceImpl extends Breadcrumbs implements ToyService {
    private final ToyRepository toyRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Autowired
    public ToyServiceImpl(ToyRepository toyRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.toyRepository = toyRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    public String displayProductAddToyPage(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("toy.text", null, locale);

        model.addAttribute("linkType", "toy");
        model.addAttribute("productType", productTypeMessage);

        model.addAttribute("product", new ToyAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("toy"));
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddToy(ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("toy.text", null, locale);

        model.addAttribute("linkType", "toy");
        model.addAttribute("productType", productTypeMessage);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("toy"));
            return PRODUCT_ADD_HTML;
        }

        addToyToDatabase(toyAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Toy added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllToysPage(Model model, String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("toys.text", null, locale);

        addProductBreadcrumb(model, null, productTypeMessage);
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "toys");

        List<Toy> toyList;

        if (sort == null) {
            toyList = this.toyRepository.findAll();
        } else if (sort.equals("lowest")) {
            toyList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            toyList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            toyList = getAllSortedAlphabetically();
        } else {
            toyList = this.toyRepository.findAll();
        }

        model.addAttribute("productList", toyList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewToyPage(Long id, Model model) {
        Toy toy = toyRepository.findToyById(id);

        addProductBreadcrumb(model, null, "Toy", toy.getProductName());
        model.addAttribute("product", toy);
        model.addAttribute("productDetails", null);

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
    private List<Toy> getAllSortedByLowestPrice() {
        return toyRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Toy> getAllSortedByHighestPrice() {
        return toyRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Toy> getAllSortedAlphabetically() {
        return toyRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }

    private void addToyToDatabase(ToyAddDTO toyAddDTO) {
        Toy toy = modelMapper.map(toyAddDTO, Toy.class);

        toy.setProductType(ProductTypeEnum.TEXTBOOK);
        toy.setDateCreated(LocalDate.now());
        toy.setProductType(ProductTypeEnum.TOY);

        toyRepository.saveAndFlush(toy);

    }
}
