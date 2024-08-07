package com.project.EpicByte.service.product;

import com.project.EpicByte.model.dto.productDTOs.TextbookAddDTO;
import com.project.EpicByte.model.entity.productEntities.Textbook;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.TextbookRepository;
import com.project.EpicByte.service.impl.productImpl.TextbookServiceImpl;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.TextbookService;
import com.project.EpicByte.util.Breadcrumbs;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TextbookServiceTest {
    private TextbookService textbookService;
    @Mock
    TextbookRepository textbookRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductImagesService productImagesService;

    @Mock
    private Breadcrumbs breadcrumbs;

    @Mock
    private Model model;

    private BindingResult bindingResult;
    private TextbookAddDTO textbookAddDTO;
    private Textbook textbook;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        FieldNamesGenerator fieldNamesGenerator = new FieldNamesGenerator(messageSource);
        this.breadcrumbs = new Breadcrumbs(messageSource);

        this.textbookService = new TextbookServiceImpl(textbookRepository,
                cartRepository,
                modelMapper,
                messageSource,
                breadcrumbs,
                fieldNamesGenerator,
                productImagesService);

        this.bindingResult = mock(BindingResult.class);

        this.textbookAddDTO = new TextbookAddDTO();
        textbookAddDTO.setProductImageUrl("testURK");
        textbookAddDTO.setProductName("testName");
        textbookAddDTO.setProductPrice(10.00);
        textbookAddDTO.setDescription("testDescription");
        textbookAddDTO.setAuthorName("testAuthor");
        textbookAddDTO.setPublisher("testPublisher");
        textbookAddDTO.setPublicationDate(LocalDate.now().minusYears(1));
        textbookAddDTO.setLanguage("English");
        textbookAddDTO.setPrintLength(100);
        textbookAddDTO.setDimensions("testDimensions");

        this.textbook = modelMapper.map(textbookAddDTO, Textbook.class);
    }

    @Test
    public void displayProductAddTextbookPage_success() {
        String actualPage = this.textbookService.displayProductAddTextbookPage(model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void handleProductAddTextbook_success() {
        String actualPage = this.textbookService.handleProductAddTextbook(textbookAddDTO, bindingResult, model);
        Assertions.assertEquals(DISPLAY_TEXT_HTML, actualPage);
    }

    @Test
    public void handleProductAddTextbook_fail_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String actualPage = this.textbookService.handleProductAddTextbook(new TextbookAddDTO(), bindingResult, model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void displayAllTextbooksPage_success() {
        String actualPage = this.textbookService.displayAllTextbooksPage(this.model, "default");
        Assertions.assertEquals(PRODUCTS_ALL_HTML, actualPage);
    }

    @Test
    public void displayDetailedViewTextbookPage_success() {
        when(textbookRepository.findTextbookById(any())).thenReturn(textbook);
        String actualPage = this.textbookService.displayDetailedViewTextbookPage(UUID.randomUUID(), model);
        Assertions.assertEquals(PRODUCT_DETAILS_HTML, actualPage);
    }

    @Test
    public void deleteTextbook_success() {
        when(textbookRepository.findTextbookById(any())).thenReturn(textbook);
        when(productImagesService.removeImageURL(any())).thenReturn(true);
        String actualPage = this.textbookService.deleteTextbook(UUID.randomUUID());
        Assertions.assertEquals("redirect:" + ALL_TEXTBOOKS_URL, actualPage);
    }
}
