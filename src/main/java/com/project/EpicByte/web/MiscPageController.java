package com.project.EpicByte.web;

import com.project.EpicByte.util.Breadcrumbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

import static com.project.EpicByte.util.Constants.*;

/**
 * The MiscPageController class is responsible for handling requests related to miscellaneous pages, such as
 * terms and conditions and privacy policy pages.

 * While this break the Best-Practices approach, it would otherwise be unpractical
 * to extract these 2 GET requests in separate controllers, or even create respective services with 1 simple method per.
 */

@Controller
public class MiscPageController extends Breadcrumbs {
    private final MessageSource messageSource;

    @Autowired
    public MiscPageController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping(TERMS_AND_CONDITIONS_URL)
    public String displayTermsAndConditionsPage(Model model) {
        addProductBreadcrumb(model, TERMS_AND_CONDITIONS_URL, "Terms and Conditions");
        model.addAttribute("pageType", getLocalizedText("terms.and.conditions.text"));
        return DISPLAY_TEXT_HTML;
    }

    @GetMapping(PRIVACY_URL)
    public String displayPrivacyPage(Model model) {
        addProductBreadcrumb(model, PRIVACY_URL, "Privacy");
        model.addAttribute("pageType", getLocalizedText("privacy.text"));
        return DISPLAY_TEXT_HTML;
    }

    // Support method
    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }
}
