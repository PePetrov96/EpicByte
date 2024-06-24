package com.project.EpicByte.service.impl.productServices;

import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.ToyRepository;
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
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final ProductImagesService productImagesService;

    @Autowired
    public ToyServiceImpl(ToyRepository toyRepository,
                          CartRepository cartRepository,
                          ModelMapper modelMapper,
                          MessageSource messageSource,
                          ProductImagesService productImagesService) {
        this.toyRepository = toyRepository;
        this.cartRepository = cartRepository;
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
        if (toy == null) return returnErrorPage(model);

        addProductBreadcrumb(model, ALL_TOYS_URL, "Toys", toy.getProductName());
        model.addAttribute("product", toy);
        model.addAttribute("productDetails", getDetailFields(toy)); //TODO: all details
        model.addAttribute("linkType", "toys");

        return PRODUCT_DETAILS_HTML;
    }

    @Override
    public String deleteToy(UUID id) {
        deleteToyFromDatabase(id);
        return "redirect:" + ALL_TOYS_URL;
    }

    private void deleteToyFromDatabase(UUID id) {
        Toy toy = toyRepository.findToyById(id);

        // Remove the image from Cloudinary
        this.productImagesService.removeImageURL(toy.getProductImageUrl());

        // Remove the image from the repository
        this.toyRepository.delete(toy);

        // Remove the product from all user carts
        List<CartItem> cartItemList = this.cartRepository.findAllByProductId(id);
        this.cartRepository.deleteAll(cartItemList);
    }

    // Support methods
    private void deleteMovieFromDatabase(UUID id) {}

    private String returnErrorPage(Model model) {
        model.addAttribute("errorType", "Oops...");
        model.addAttribute("errorText", "Something went wrong!");
        return ERROR_PAGE_HTML;
    }

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
