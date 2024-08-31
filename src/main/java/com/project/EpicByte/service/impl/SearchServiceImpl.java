package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.entity.BaseProduct;
import com.project.EpicByte.repository.productRepositories.*;
import com.project.EpicByte.service.SearchService;
import com.project.EpicByte.util.Breadcrumbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.project.EpicByte.util.Constants.*;

@Service
public class SearchServiceImpl implements SearchService {
    private final BookRepository bookRepository;
    private final TextbookRepository textbookRepository;
    private final MusicRepository musicRepository;
    private final MovieRepository movieRepository;
    private final ToyRepository toyRepository;

    private final Breadcrumbs breadcrumbs;
    private final MessageSource messageSource;

    @Autowired
    public SearchServiceImpl(BookRepository bookRepository, TextbookRepository textbookRepository, MusicRepository musicRepository, MovieRepository movieRepository, ToyRepository toyRepository, Breadcrumbs breadcrumbs, MessageSource messageSource) {
        this.bookRepository = bookRepository;
        this.textbookRepository = textbookRepository;
        this.musicRepository = musicRepository;
        this.movieRepository = movieRepository;
        this.toyRepository = toyRepository;
        this.breadcrumbs = breadcrumbs;
        this.messageSource = messageSource;
    }

    @Override
    public String showSearchResults(String query, Model model) {
        addDefaultModelAttributesForAllAndDetailed(model);

        List<BaseProduct> productList = findAllProducts(query);

        model.addAttribute("productList", productList);
        return SEARCH_RESULTS_HTML;
    }

    private List<BaseProduct> findAllProducts(String query) {
        List<BaseProduct> productList = new ArrayList<>();

        productList.addAll(bookRepository.findBooksByAuthorName(query));
        productList.addAll(bookRepository.findBooksByProductName(query));

        productList.addAll(textbookRepository.findTextbooksByAuthorName(query));
        productList.addAll(textbookRepository.findTextbooksByProductName(query));

        productList.addAll(musicRepository.findMusicByProductName(query));
        productList.addAll(musicRepository.findMusicByArtistName(query));

        productList.addAll(movieRepository.findMoviesByProductName(query));

        productList.addAll(toyRepository.findToysByProductName(query));

        return productList;
    }

    private void addDefaultModelAttributesForAllAndDetailed(Model model) {
        breadcrumbs.addProductBreadcrumb(model, SEARCH_RESULTS_HTML, "Results");
        model.addAttribute("productType", getLocalizedText("results.text"));
        model.addAttribute("linkType", "books");
    }

    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }
}
