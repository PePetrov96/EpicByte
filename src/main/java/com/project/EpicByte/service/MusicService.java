package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.MusicAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface MusicService {
    String displayProductAddMusicPage(Model model);
    String handleProductAddMusic(MusicAddDTO musicAddDTO, BindingResult bindingResult, Model model);
}
