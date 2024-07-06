package com.project.EpicByte.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A utility class responsible for adding breadcrumb data to a Model object, by layering links and map elements.
 */

@Component
public class Breadcrumbs {
    private final MessageSource messageSource;

    @Autowired
    public Breadcrumbs(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void addProductBreadcrumb(Model model, String pageUrl, String... pageNames) {
        Map<String, String> breadcrumbs = new LinkedHashMap<>();
        breadcrumbs.put(getPageText("Home"), "/");

        for (int i = 0; i < pageNames.length; i++) {
            if (i == 0) {
                breadcrumbs.put(getPageText(pageNames[i]), pageUrl);
            } else {
                breadcrumbs.put(pageNames[i], "");
            }
        }

        model.addAttribute("breadcrumbs", breadcrumbs);
    }

    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public String getPageText(String pageName) {
        Locale locale = LocaleContextHolder.getLocale();
        String pageNameText = pageName.toLowerCase().replace("/", "").replaceAll(" ", ".");
        return messageSource.getMessage(pageNameText + ".text", null, locale);
    }
}
