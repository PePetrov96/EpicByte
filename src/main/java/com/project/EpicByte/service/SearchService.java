package com.project.EpicByte.service;

import org.springframework.ui.Model;

public interface SearchService {
    String showSearchResults(String query, Model model);
}
