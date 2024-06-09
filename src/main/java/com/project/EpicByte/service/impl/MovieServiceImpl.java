package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.MovieAddDTO;
import com.project.EpicByte.model.dto.MusicAddDTO;
import com.project.EpicByte.model.entity.enums.LanguageEnum;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Movie;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.MovieRepository;
import com.project.EpicByte.service.MovieService;
import com.project.EpicByte.util.FieldNamesGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static com.project.EpicByte.util.Constants.DISPLAY_TEXT_HTML;
import static com.project.EpicByte.util.Constants.PRODUCT_ADD_HTML;

@Service
public class MovieServiceImpl extends FieldNamesGenerator implements MovieService {
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ModelMapper modelMapper) {
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String displayProductAddMoviePage(Model model) {
        model.addAttribute("productType", "movie");
        model.addAttribute("product", new MovieAddDTO());
        model.addAttribute("fieldsMap", getFieldNames("movie"));
        model.addAttribute("enumsList", LanguageEnum.values());
        return PRODUCT_ADD_HTML;
    }

    @Override
    public String handleProductAddMovie(MovieAddDTO movieAddDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("productType", "movie");

        if (bindingResult.hasErrors()) {
            model.addAttribute("fieldsMap", getFieldNames("movie"));
            model.addAttribute("enumsList", LanguageEnum.values());
            return PRODUCT_ADD_HTML;
        }

        addMovieToDatabase(movieAddDTO);

        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", "Movie added successfully!");
        return DISPLAY_TEXT_HTML;
    }

    // Support methods
    private void addMovieToDatabase(MovieAddDTO movieAddDTO) {
        Movie movie = modelMapper.map(movieAddDTO, Movie.class);

        movie.setProductType(ProductTypeEnum.MOVIE);
        movieRepository.saveAndFlush(movie);
    }
}
