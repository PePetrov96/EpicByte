package com.project.EpicByte.service.impl.productImpl;

import com.project.EpicByte.exceptions.NoSuchProductException;
import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.TextbookRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.TextbookService;
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
public class TextbookServiceImpl implements TextbookService {
    private final TextbookRepository textbookRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final Breadcrumbs breadcrumbs;
    private final FieldNamesGenerator fieldNamesGenerator;
    // CLOUDINARY
    private final ProductImagesService productImagesService;

    @Autowired
    public TextbookServiceImpl(TextbookRepository textbookRepository,
                               CartRepository cartRepository,
                               ModelMapper modelMapper,
                               MessageSource messageSource,
                               Breadcrumbs breadcrumbs,
                               FieldNamesGenerator fieldNamesGenerator,
                               ProductImagesService productImagesService) {
        this.textbookRepository = textbookRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.breadcrumbs = breadcrumbs;
        this.fieldNamesGenerator = fieldNamesGenerator;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddTextbookPage(Model model) {
        addDefaultModelAttributesForAddAndHandle(model);
        model.addAttribute("product", new TextbookAddDTO());
        model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("textbook", false));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddTextbook(TextbookAddDTO textbookAddDTO, BindingResult bindingResult, Model model) {
        addDefaultModelAttributesForAddAndHandle(model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("textbook", false));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addTextbookToDatabase(textbookAddDTO, model);
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllTextbooksPage(Model model, String sort) {
        breadcrumbs.addProductBreadcrumb(model, ALL_TEXTBOOKS_URL, "Textbooks");
        addDefaultModelAttributesForAllAndDetailed(model);
        List<Textbook> textbookList = getSortedTextbooks(sort);
        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));
        model.addAttribute("productList", textbookList);
        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewTextbookPage(UUID id, Model model) {
        Textbook textbook = getTextbookOrThrowException(id);
        breadcrumbs.addProductBreadcrumb(model, ALL_TEXTBOOKS_URL, "Textbooks", textbook.getProductName());
        addDefaultModelAttributesForAllAndDetailed(model);
        model.addAttribute("product", textbook);
        model.addAttribute("productDetails", getDetailFields(textbook));
        model.addAttribute("linkType", "textbooks");
        return PRODUCT_DETAILS_HTML;
    }

    @Override
    public String deleteTextbook(UUID id) {
        deleteTextbookFromDatabase(id);
        return "redirect:" + ALL_TEXTBOOKS_URL;
    }

    // Support methods
    private Textbook getTextbookOrThrowException(UUID id){
        Textbook textbook = textbookRepository.findTextbookById(id);
        if (textbook == null) throw new NoSuchProductException();
        return textbook;
    }

    private void addDefaultModelAttributesForAddAndHandle(Model model) {
        model.addAttribute("linkType", "textbook");
        model.addAttribute("productType", getLocalizedText("textbook.text"));
    }

    private void addDefaultModelAttributesForAllAndDetailed(Model model) {
        model.addAttribute("productType", getLocalizedText("textbooks.text"));
        model.addAttribute("productLinkType", "textbook");
        model.addAttribute("linkType", "textbooks");
    }

    private List<Textbook> getSortedTextbooks(String sort) {
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

    private void deleteTextbookFromDatabase(UUID id) {
        Textbook textbook = textbookRepository.findTextbookById(id);

        // Remove the image from Cloudinary
        this.productImagesService.removeImageURL(textbook.getProductImageUrl());

        // Remove the image from the repository
        this.textbookRepository.delete(textbook);

        // Remove the product from all user carts
        List<CartItem> cartItemList = this.cartRepository.findAllByProductId(id);
        this.cartRepository.deleteAll(cartItemList);
    }

    private Map<String, String> getDetailFields(Textbook textbook) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("authorName.text"), textbook.getAuthorName());
        fieldsMap.put(getLocalizedText("publisher.text"), textbook.getPublisher());
        fieldsMap.put(getLocalizedText("publicationDate.text"), textbook.getPublicationDate().toString());
        fieldsMap.put(getLocalizedText("language.text"), textbook.getLanguage().toString());
        fieldsMap.put(getLocalizedText("printLength.text"), textbook.getPrintLength().toString() + " pages");
        fieldsMap.put(getLocalizedText("dimensions.text"), textbook.getDimensions());

        return fieldsMap;
    }


    private void addTextbookToDatabase(TextbookAddDTO textbookAddDTO, Model model) {
        Textbook textbook = modelMapper.map(textbookAddDTO, Textbook.class);
        setNewTextbookDetails(textbook, textbookAddDTO);
        textbookRepository.saveAndFlush(textbook);
        setSuccessMessageToModel(model);
    }

    private void setNewTextbookDetails(Textbook textbook, TextbookAddDTO textbookAddDTO) {
        textbook.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                textbookAddDTO.getProductImageUrl()));

        textbook.setNewProduct(true);
        textbook.setDateCreated(LocalDate.now());
        textbook.setProductType(ProductTypeEnum.TEXTBOOK);
    }

    private void setSuccessMessageToModel(Model model) {
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("textbook.added.successfully.text"));
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private List<Textbook> getAllSortedByIsNewProduct() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.DESC,"isNewProduct"));
    }

    private List<Textbook> getAllSortedByLowestPrice() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Textbook> getAllSortedByHighestPrice() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Textbook> getAllSortedAlphabetically() {
        return textbookRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }
}
