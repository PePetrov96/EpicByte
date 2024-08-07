package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.productDTOs.MovieAddDTO;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.UUID;

public interface MovieService {
    String displayProductAddMoviePage(Model model);
    String handleProductAddMovie(MovieAddDTO movieAddDTO, BindingResult bindingResult, Model model);
    String displayAllMoviesPage(Model model, String string);
    String displayDetailedViewMoviePage(UUID id, Model model);
    String deleteMovie(UUID id);
}
