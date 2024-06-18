package com.project.EpicByte.service.impl.productServices;

import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.ToyRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.productServices.ToyService;
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
import java.util.*;

import static com.project.EpicByte.util.Constants.*;

@Service
public class ToyServiceImpl extends Breadcrumbs implements ToyService {
    private final ToyRepository toyRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final ProductImagesService productImagesService;

    @Autowired
    public ToyServiceImpl(ToyRepository toyRepository, ModelMapper modelMapper, MessageSource messageSource,
                          ProductImagesService productImagesService) {
        this.toyRepository = toyRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddToyPage(Model model) {
        model.addAttribute("linkType", "toy");
        model.addAttribute("productType", getLocalizedText("toy.text"));
        model.addAttribute("product", new ToyAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("toy", false));
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddToy(ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("linkType", "toy");
        model.addAttribute("productType", getLocalizedText("toy.text"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("toy", false));
            return PRODUCT_ADD_HTML;
        }

        addToyToDatabase(toyAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Toy added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllToysPage(Model model, String sort) {
        addProductBreadcrumb(model, ALL_TOYS_URL, "Toys");
        model.addAttribute("productType", getLocalizedText("toys.text"));
        model.addAttribute("productLinkType", "toy");
        model.addAttribute("linkType", "toys");

        List<Toy> toyList;

        if (sort == null) {
            toyList = getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            toyList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            toyList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            toyList = getAllSortedAlphabetically();
        } else {
            toyList = getAllSortedByIsNewProduct();
        }

        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));

        model.addAttribute("productList", toyList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewToyPage(UUID id, Model model) {
        Toy toy = toyRepository.findToyById(id);

        addProductBreadcrumb(model, ALL_TOYS_URL, "Toys", toy.getProductName());
        model.addAttribute("product", toy);
        model.addAttribute("productDetails", getDetailFields(toy)); //TODO: all details

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
    private Map<String, String> getDetailFields(Toy toy) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("brand.text"), toy.getBrand());

        return fieldsMap;
    }

    private void addToyToDatabase(ToyAddDTO toyAddDTO) {
        Toy toy = modelMapper.map(toyAddDTO, Toy.class);

        // CLOUDINARY
        toy.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                toyAddDTO.getProductImageUrl()));

        toy.setProductType(ProductTypeEnum.TEXTBOOK);
        toy.setDateCreated(LocalDate.now());
        toy.setProductType(ProductTypeEnum.TOY);

        toyRepository.saveAndFlush(toy);
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private List<Toy> getAllSortedByIsNewProduct() {
        return toyRepository.findAll(Sort.by(Sort.Direction.DESC,"isNewProduct"));
    }

    private List<Toy> getAllSortedByLowestPrice() {
        return toyRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Toy> getAllSortedByHighestPrice() {
        return toyRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Toy> getAllSortedAlphabetically() {
        return toyRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }
}
