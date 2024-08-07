package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.MusicAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.UUID;

public interface MusicService {
    String displayProductAddMusicPage(Model model);
    String handleProductAddMusic(MusicAddDTO musicAddDTO, BindingResult bindingResult, Model model);
    String displayAllMusicPage(Model model, String string);
    String displayDetailedViewMusicPage(UUID id, Model model);
    String deleteMusic(UUID id);
}
