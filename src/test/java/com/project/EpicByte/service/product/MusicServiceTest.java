package com.project.EpicByte.service.product;

import com.project.EpicByte.model.dto.productDTOs.MusicAddDTO;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.MusicRepository;
import com.project.EpicByte.service.impl.productImpl.MusicServiceImpl;
import com.project.EpicByte.service.MusicService;
import com.project.EpicByte.service.ProductImagesService;
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
public class MusicServiceTest {
    private MusicService musicService;

    @Mock
    MusicRepository musicRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ProductImagesService productImagesService;

    @Mock
    private Breadcrumbs breadcrumbs;

    @Mock
    private Model model;

    private BindingResult bindingResult;

    private MusicAddDTO musicAddDTO;
    private Music music;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");

        FieldNamesGenerator fieldNamesGenerator = new FieldNamesGenerator(messageSource);

        this.musicService = new MusicServiceImpl(musicRepository,
                cartRepository,
                modelMapper,
                messageSource,
                breadcrumbs,
                fieldNamesGenerator,
                productImagesService);

        this.breadcrumbs = new Breadcrumbs(messageSource);
        this.bindingResult = mock(BindingResult.class);

        this.musicAddDTO = new MusicAddDTO();
        musicAddDTO.setProductImageUrl("testURK");
        musicAddDTO.setProductName("testName");
        musicAddDTO.setProductPrice(10.00);
        musicAddDTO.setDescription("testDescription");

        musicAddDTO.setArtistName("testArtist");
        musicAddDTO.setPublisher("testPublisher");
        musicAddDTO.setPublicationDate(LocalDate.now().minusYears(1));
        musicAddDTO.setCarrier("CD");
        musicAddDTO.setGenre("testGenre");

        this.music = modelMapper.map(musicAddDTO, Music.class);
    }

    @Test
    public void displayProductAddMusicPage_success() {
        String actualPage = this.musicService.displayProductAddMusicPage(model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void handleProductAddMusic_success() {
        String actualPage = this.musicService.handleProductAddMusic(musicAddDTO, bindingResult, model);
        Assertions.assertEquals(DISPLAY_TEXT_HTML, actualPage);
    }

    @Test
    public void handleProductAddMusic_fail_bindingResultHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String actualPage = this.musicService.handleProductAddMusic(new MusicAddDTO(), bindingResult, model);
        Assertions.assertEquals(PRODUCT_ADD_HTML, actualPage);
    }

    @Test
    public void displayAllMusicPage_success() {
        String actualPage = this.musicService.displayAllMusicPage(this.model, "default");
        Assertions.assertEquals(PRODUCTS_ALL_HTML, actualPage);
    }

    @Test
    public void displayDetailedViewMusicPage_success() {
        when(musicRepository.findMusicById(any())).thenReturn(music);
        String actualPage = this.musicService.displayDetailedViewMusicPage(UUID.randomUUID(), model);
        Assertions.assertEquals(PRODUCT_DETAILS_HTML, actualPage);
    }

    @Test
    public void deleteMusic_success() {
        when(musicRepository.findMusicById(any())).thenReturn(music);
        when(productImagesService.removeImageURL(any())).thenReturn(true);
        String actualPage = this.musicService.deleteMusic(UUID.randomUUID());
        Assertions.assertEquals("redirect:" + ALL_MUSIC_URL, actualPage);
    }
}
