package com.project.EpicByte.service.impl.productImpl;

import com.project.EpicByte.exceptions.NoSuchProductException;
import com.project.EpicByte.model.dto.productDTOs.MusicAddDTO;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.productRepositories.MusicRepository;
import com.project.EpicByte.service.ProductImagesService;
import com.project.EpicByte.service.MusicService;
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
public class MusicServiceImpl implements MusicService {
    private final MusicRepository musicRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final Breadcrumbs breadcrumbs;
    private final FieldNamesGenerator fieldNamesGenerator;
    // CLOUDINARY
    private final ProductImagesService productImagesService;

    @Autowired
    public MusicServiceImpl(MusicRepository musicRepository,
                            CartRepository cartRepository,
                            ModelMapper modelMapper,
                            MessageSource messageSource,
                            Breadcrumbs breadcrumbs,
                            FieldNamesGenerator fieldNamesGenerator,
                            ProductImagesService productImagesService) {
        this.musicRepository = musicRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.breadcrumbs = breadcrumbs;
        this.fieldNamesGenerator = fieldNamesGenerator;
        this.productImagesService = productImagesService;
    }

    @Override
    public String displayProductAddMusicPage(Model model) {
        addDefaultModelAttributesForAddAndHandle(model);
        model.addAttribute("product", new MusicAddDTO());
        model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("music", false));
        model.addAttribute("enumsList", MusicCarrierEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMusic(MusicAddDTO musicAddDTO, BindingResult bindingResult, Model model) {
        addDefaultModelAttributesForAddAndHandle(model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", fieldNamesGenerator.getFieldNames("music", false));
            model.addAttribute("enumsList", MusicCarrierEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addMusicToDatabase(musicAddDTO, model);
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllMusicPage(Model model, String sort) {
        breadcrumbs.addProductBreadcrumb(model, ALL_MUSIC_URL, "Music");
        addDefaultModelAttributesForAllAndDetailed(model);
        List<Music> musicList = getSortedMusic(sort);
        model.addAttribute("selectedSortingOption", Objects.requireNonNullElse(sort, "default"));
        model.addAttribute("productList", musicList);
        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewMusicPage(UUID id, Model model) {
        Music music = getMusicOrThrowException(id);
        breadcrumbs.addProductBreadcrumb(model, ALL_MUSIC_URL, "Music", music.getProductName());
        addDefaultModelAttributesForAllAndDetailed(model);
        model.addAttribute("product", music);
        model.addAttribute("productDetails", getDetailFields(music));
        model.addAttribute("linkType", "music");
        return PRODUCT_DETAILS_HTML;
    }

    @Override
    public String deleteMusic(UUID id) {
        deleteMusicFromDatabase(id);
        return "redirect:" + ALL_MUSIC_URL;
    }

    // Support methods
    private Music getMusicOrThrowException(UUID id){
        Music music = musicRepository.findMusicById(id);
        if (music == null) throw new NoSuchProductException();
        return music;
    }

    private void addDefaultModelAttributesForAddAndHandle(Model model) {
        model.addAttribute("linkType", "music");
        model.addAttribute("productType", getLocalizedText("music.text"));
    }

    private void addDefaultModelAttributesForAllAndDetailed(Model model) {
        model.addAttribute("productType", getLocalizedText("music.text"));
        model.addAttribute("productLinkType", "music");
        model.addAttribute("linkType", "music");
    }

    private List<Music> getSortedMusic(String sort) {
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

    private void deleteMusicFromDatabase(UUID id) {
        Music music = this.musicRepository.findMusicById(id);

        // Remove the image from Cloudinary
        this.productImagesService.removeImageURL(music.getProductImageUrl());

        // Remove the image from the repository
        this.musicRepository.delete(music);

        // Remove the product from all user carts
        List<CartItem> cartItemList = this.cartRepository.findAllByProductId(id);
        this.cartRepository.deleteAll(cartItemList);
    }

    private Map<String, String> getDetailFields(Music music) {
        LinkedHashMap<String , String> fieldsMap = new LinkedHashMap<>();

        fieldsMap.put(getLocalizedText("artistName.text"), music.getArtistName());
        fieldsMap.put(getLocalizedText("publisher.text"), music.getPublisher());
        fieldsMap.put(getLocalizedText("publicationDate.text"), music.getPublicationDate().toString());
        fieldsMap.put(getLocalizedText("carrier.text"), music.getCarrier().toString());
        fieldsMap.put(getLocalizedText("genre.text"), music.getGenre());

        return fieldsMap;
    }

    private void addMusicToDatabase(MusicAddDTO musicAddDTO, Model model) {
        Music music = modelMapper.map(musicAddDTO, Music.class);
        setNewMusicDetails(music, musicAddDTO);
        musicRepository.saveAndFlush(music);
        setSuccessMessageToModel(model);
    }

    private void setNewMusicDetails(Music music, MusicAddDTO musicAddDTO) {
        music.setProductImageUrl(
                this.productImagesService
                        .getImageURL(
                                musicAddDTO.getProductImageUrl()));
        music.setNewProduct(true);
        music.setDateCreated(LocalDate.now());
        music.setProductType(ProductTypeEnum.MUSIC);
    }

    private void setSuccessMessageToModel(Model model) {
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("music.added.successfully.text"));
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }

    private List<Music> getAllSortedByIsNewProduct() {
        return musicRepository.findAll(Sort.by(Sort.Direction.DESC,"isNewProduct"));
    }

    private List<Music> getAllSortedByLowestPrice() {
        return musicRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Music> getAllSortedByHighestPrice() {
        return musicRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Music> getAllSortedAlphabetically() {
        return musicRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }
}
