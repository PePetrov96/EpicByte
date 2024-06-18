package com.project.EpicByte.service.impl.productServices;

import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.repository.TextbookRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.productServices.TextbookService;
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
public class TextbookServiceImpl extends Breadcrumbs implements TextbookService {
    private final TextbookRepository textbookRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final ProductImagesService productImagesService;

    @Autowired
    public TextbookServiceImpl(TextbookRepository textbookRepository, ModelMapper modelMapper,
                               MessageSource messageSource, ProductImagesService productImagesService) {
        this.textbookRepository = textbookRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddTextbookPage(Model model) {
        model.addAttribute("linkType", "textbook");
        model.addAttribute("productType", getLocalizedText("textbook.text"));
        model.addAttribute("product", new TextbookAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("textbook", false));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddTextbook(TextbookAddDTO textbookAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", getLocalizedText("textbook.text"));
        model.addAttribute("linkType", "textbook");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("textbook", false));
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
        addProductBreadcrumb(model, ALL_TEXTBOOKS_URL, "Textbooks");
        model.addAttribute("productType", getLocalizedText("textbooks.text"));
        model.addAttribute("productLinkType", "textbook");
        model.addAttribute("linkType", "textbooks");

        List<Textbook> textbookList;

        if (sort == null) {
            textbookList = getAllSortedByIsNewProduct();
        } else if (sort.equals("lowest")) {
            textbookList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            textbookList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            textbookList = getAllSortedAlphabetically();
        } else {
            textbookList = getAllSortedByIsNewProduct();
        }

        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));

        model.addAttribute("productList", textbookList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewTextbookPage(UUID id, Model model) {
        Textbook textbook = textbookRepository.findTextbookById(id);

        addProductBreadcrumb(model, ALL_TEXTBOOKS_URL, "Textbooks", textbook.getProductName());
        model.addAttribute("product", textbook);
        model.addAttribute("productDetails", getDetailFields(textbook)); //TODO: all details

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
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

    private void addTextbookToDatabase(TextbookAddDTO textbookAddDTO) {
        Textbook textbook = modelMapper.map(textbookAddDTO, Textbook.class);

        // CLOUDINARY
        textbook.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                textbookAddDTO.getProductImageUrl()));
        textbook.setProductType(ProductTypeEnum.TEXTBOOK);
        textbook.setDateCreated(LocalDate.now());
        textbook.setNewProduct(true);

        textbookRepository.saveAndFlush(textbook);

    }
}
