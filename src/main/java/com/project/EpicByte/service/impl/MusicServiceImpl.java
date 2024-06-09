package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.BookAddDTO;
import com.project.EpicByte.model.dto.MusicAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Book;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.MusicRepository;
import com.project.EpicByte.service.MusicService;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.project.EpicByte.util.Constants.*;

@Service
public class MusicServiceImpl extends FieldNamesGenerator implements MusicService {
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MusicServiceImpl(MusicRepository musicRepository, ModelMapper modelMapper) {
        this.musicRepository = musicRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public String displayProductAddMusicPage(Model model) {
        model.addAttribute("productType", "music");
        model.addAttribute("product", new MusicAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("music"));
        model.addAttribute("enumsList", MusicCarrierEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMusic(MusicAddDTO musicAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", "music");

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

    // Support methods
    private void addMusicToDatabase(MusicAddDTO musicAddDTO) {
        Music music = modelMapper.map(musicAddDTO, Music.class);

        music.setProductType(ProductTypeEnum.MUSIC);
        musicRepository.saveAndFlush(music);

    }
}
