package com.project.EpicByte.service.product;

import com.project.EpicByte.model.dto.productDTOs.ToyAddDTO;
import com.project.EpicByte.model.entity.productEntities.Toy;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.ToyRepository;
import com.project.EpicByte.service.impl.productImpl.ToyServiceImpl;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.ToyService;
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

import java.util.UUID;

import static com.project.EpicByte.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToyServiceTest {
    private ToyService toyService;
    @Mock
    ToyRepository toyRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductImagesService productImagesService;

    @Mock
    private Breadcrumbs breadcrumbs;

    @Mock
    private Model model;

    private BindingResult bindingResult;
    private ToyAddDTO toyAddDTO;
    private Toy toy;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        FieldNamesGenerator fieldNamesGenerator = new FieldNamesGenerator(messageSource);
        this.breadcrumbs = new Breadcrumbs(messageSource);

        this.toyService = new ToyServiceImpl(toyRepository,
                cartRepository,
                modelMapper,
                messageSource,
                breadcrumbs,
                fieldNamesGenerator,
                productImagesService);

        this.bindingResult = mock(BindingResult.class);

        this.toyAddDTO = new ToyAddDTO();
        toyAddDTO.setProductImageUrl("testURK");
        toyAddDTO.setProductName("testName");
        toyAddDTO.setProductPrice(10.00);
        toyAddDTO.setDescription("testDescription");
        toyAddDTO.setBrand("testBrand");

        this.toy = modelMapper.map(toyAddDTO, Toy.class);
    }

    @Test
    public void displayProductAddToyPage_success() {
        String actualPage = this.toyService.displayProductAddToyPage(model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void handleProductAddToy_success() {
        String actualPage = this.toyService.handleProductAddToy(toyAddDTO, bindingResult, model);
        Assertions.assertEquals(DISPLAY_TEXT_HTML, actualPage);
    }

    @Test
    public void handleProductAddToy_fail_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String actualPage = this.toyService.handleProductAddToy(new ToyAddDTO(), bindingResult, model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void displayAllToysPage_success() {
        String actualPage = this.toyService.displayAllToysPage(this.model, "default");
        Assertions.assertEquals(PRODUCTS_ALL_HTML, actualPage);
    }

    @Test
    public void displayDetailedViewToyPage_success() {
        when(toyRepository.findToyById(any())).thenReturn(toy);
        String actualPage = this.toyService.displayDetailedViewToyPage(UUID.randomUUID(), model);
        Assertions.assertEquals(PRODUCT_DETAILS_HTML, actualPage);
    }

    @Test
    public void deleteToy_success() {
        when(toyRepository.findToyById(any())).thenReturn(toy);
        when(productImagesService.removeImageURL(any())).thenReturn(true);
        String actualPage = this.toyService.deleteToy(UUID.randomUUID());
        Assertions.assertEquals("redirect:" + ALL_TOYS_URL, actualPage);
    }
}
