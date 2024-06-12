package com.project.EpicByte.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A utility class responsible for adding breadcrumb data to a Model object, by layering links and map elements.
 */

public class Breadcrumbs extends FieldNamesGenerator {
    @Autowired
    private MessageSource messageSource;

    protected void addProductBreadcrumb(Model model, String pageUrl, String... pageNames) {
        Map<String, String> breadcrumbs = new LinkedHashMap<>();
        breadcrumbs.put(getPageText("Home"), "/");

        if (pageUrl == null) {
            int length = pageNames.length;
            if(length > 1){
                // For category like "Book"
                breadcrumbs.put(getPageText(pageNames[length - 2]), "/products/" + pageNames[length - 2].toLowerCase());
            }
            // For product name, no link as it's the active page
            breadcrumbs.put(pageNames[length - 1], "");

        } else {
            breadcrumbs.put(pageNames[0], pageUrl);
        }

        model.addAttribute("breadcrumbs", breadcrumbs);
    }

    private String getPageText(String pageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(pageName.toLowerCase() + ".text", null, locale);
    }
}
