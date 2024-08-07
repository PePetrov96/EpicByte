package com.project.EpicByte.service.impl.productImpl;

import com.project.EpicByte.exceptions.NoSuchProductException;
import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.ToyRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.ToyService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
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
public class ToyServiceImpl implements ToyService {
    private final ToyRepository toyRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final Breadcrumbs breadcrumbs;
    private final FieldNamesGenerator fieldNamesGenerator;
    // CLOUDINARY
    private final ProductImagesService productImagesService;

    @Autowired
    public ToyServiceImpl(ToyRepository toyRepository,
                          CartRepository cartRepository,
                          ModelMapper modelMapper,
                          MessageSource messageSource,
                          Breadcrumbs breadcrumbs,
                          FieldNamesGenerator fieldNamesGenerator,
                          ProductImagesService productImagesService) {
        this.toyRepository = toyRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.breadcrumbs = breadcrumbs;
        this.fieldNamesGenerator = fieldNamesGenerator;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddToyPage(Model model) {
        addDefaultModelAttributesForAddAndHandle(model);
        model.addAttribute("product", new ToyAddDTO());
        model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("toy", false));
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddToy(ToyAddDTO toyAddDTO, BindingResult bindingResult, Model model) {
        addDefaultModelAttributesForAddAndHandle(model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("toy", false));
            return PRODUCT_ADD_HTML;
        }

        addToyToDatabase(toyAddDTO, model);
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllToysPage(Model model, String sort) {
        breadcrumbs.addProductBreadcrumb(model, ALL_TOYS_URL, "Toys");
        addDefaultModelAttributesForAllAndDetailed(model);
        List<Toy> toyList = getSortedToys(sort);
        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));
        model.addAttribute("productList", toyList);
        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewToyPage(UUID id, Model model) {
        Toy toy = getToyOrThrowException(id);
        breadcrumbs.addProductBreadcrumb(model, ALL_TOYS_URL, "Toys", toy.getProductName());
        addDefaultModelAttributesForAllAndDetailed(model);
        model.addAttribute("product", toy);
        model.addAttribute("productDetails", getDetailFields(toy));
        model.addAttribute("linkType", "toys");
        return PRODUCT_DETAILS_HTML;
    }

    @Override
    public String deleteToy(UUID id) {
        deleteToyFromDatabase(id);
        return "redirect:" + ALL_TOYS_URL;
    }

    // Support methods
    private Toy getToyOrThrowException(UUID id){
        Toy toy = toyRepository.findToyById(id);
        if (toy == null) throw new NoSuchProductException();
        return toy;
    }

    private void addDefaultModelAttributesForAddAndHandle(Model model) {
        model.addAttribute("linkType", "toy");
        model.addAttribute("productType", getLocalizedText("toy.text"));
    }

    private void addDefaultModelAttributesForAllAndDetailed(Model model) {
        model.addAttribute("productType", getLocalizedText("toys.text"));
        model.addAttribute("productLinkType", "toy");
        model.addAttribute("linkType", "toys");
    }

    private List<Toy> getSortedToys(String sort) {
        if (sort == null || sort.equals("default")) {
            return getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            return getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            return getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            return getAllSortedAlphabetically();
        } else {
            return getAllSortedByIsNewProduct();
        }
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

    private Map<String, String> getDetailFields(Toy toy) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("brand.text"), toy.getBrand());

        return fieldsMap;
    }

    private void addToyToDatabase(ToyAddDTO toyAddDTO, Model model) {
        Toy toy = modelMapper.map(toyAddDTO, Toy.class);
        setNewToyDetails(toy, toyAddDTO);
        toyRepository.saveAndFlush(toy);
        setSuccessMessageToModel(model);
    }

    private void setNewToyDetails(Toy toy, ToyAddDTO toyAddDTO) {
        toy.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                toyAddDTO.getProductImageUrl()));
        toy.setNewProduct(true);
        toy.setDateCreated(LocalDate.now());
        toy.setProductType(ProductTypeEnum.TOY);
    }

    private void setSuccessMessageToModel(Model model) {
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("toy.added.successfully.text"));
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
