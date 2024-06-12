package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.MusicAddDTO;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.MusicRepository;
import com.project.EpicByte.service.MusicService;
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
public class MusicServiceImpl extends Breadcrumbs implements MusicService {
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @Autowired
    public MusicServiceImpl(MusicRepository musicRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.musicRepository = musicRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }


    @Override
    public String displayProductAddMusicPage(Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("music.text", null, locale);

        model.addAttribute("linkType", "music");
        model.addAttribute("productType", productTypeMessage);

        model.addAttribute("product", new MusicAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("music"));
        model.addAttribute("enumsList", MusicCarrierEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMusic(MusicAddDTO musicAddDTO, BindingResult bindingResult, Model model) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("music.text", null, locale);

        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "music");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("music"));
            model.addAttribute("enumsList", MusicCarrierEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addMusicToDatabase(musicAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Music added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    @Override
    public String displayAllMusicPage(Model model, String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        String productTypeMessage = messageSource.getMessage("music.text", null, locale);

        addProductBreadcrumb(model, null, productTypeMessage);
        model.addAttribute("productType", productTypeMessage);
        model.addAttribute("linkType", "music");

        List<Music> musicList;

        if (sort == null) {
            musicList = this.musicRepository.findAll();
        } else if (sort.equals("lowest")) {
            musicList = getAllSortedByLowestPrice();
        } else if (sort.equals("highest")) {
            musicList = getAllSortedByHighestPrice();
        } else if (sort.equals("alphabetical")) {
            musicList = getAllSortedAlphabetically();
        } else {
            musicList = this.musicRepository.findAll();
        }

        model.addAttribute("productList", musicList);

        return PRODUCTS_ALL_HTML;
    }

    @Override
    public String displayDetailedViewMusicPage(Long id, Model model) {
        Music music = musicRepository.findMusicById(id);

        addProductBreadcrumb(model, null, "Music", music.getProductName());
        model.addAttribute("product", music);
        model.addAttribute("productDetails", null);

        return PRODUCT_DETAILS_HTML;
    }

    // Support methods
    private List<Music> getAllSortedByLowestPrice() {
        return musicRepository.findAll(Sort.by(Sort.Direction.ASC,"productPrice"));
    }

    private List<Music> getAllSortedByHighestPrice() {
        return musicRepository.findAll(Sort.by(Sort.Direction.DESC,"productPrice"));
    }

    private List<Music> getAllSortedAlphabetically() {
        return musicRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }

    private void addMusicToDatabase(MusicAddDTO musicAddDTO) {
        Music music = modelMapper.map(musicAddDTO, Music.class);

        music.setProductType(ProductTypeEnum.MUSIC);
        music.setDateCreated(LocalDate.now());
        music.setNewProduct(true);

        musicRepository.saveAndFlush(music);

    }
}
