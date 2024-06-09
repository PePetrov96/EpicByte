package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.MovieAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface MovieService {
    String displayProductAddMoviePage(Model model);
    String handleProductAddMovie(MovieAddDTO movieAddDTO, BindingResult bindingResult, Model model);
}
