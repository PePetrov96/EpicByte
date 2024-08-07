package com.project.EpicByte.web;

import com.project.EpicByte.aop.SlowExecutionWarning;
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
public class MiscPageController {
    private final MessageSource messageSource;
    private final Breadcrumbs breadcrumbs;

    @Autowired
    public MiscPageController(MessageSource messageSource, Breadcrumbs breadcrumbs) {
        this.messageSource = messageSource;
        this.breadcrumbs = breadcrumbs;
    }

    @SlowExecutionWarning
    @GetMapping(TERMS_AND_CONDITIONS_URL)
    public String displayTermsAndConditionsPage(Model model) {
        breadcrumbs.addProductBreadcrumb(model, TERMS_AND_CONDITIONS_URL, "Terms and Conditions");
        model.addAttribute("pageType", getLocalizedText("terms.and.conditions.text"));
        return DISPLAY_TEXT_HTML;
    }

    @SlowExecutionWarning
    @GetMapping(PRIVACY_URL)
    public String displayPrivacyPage(Model model) {
        breadcrumbs.addProductBreadcrumb(model, PRIVACY_URL, "Privacy");
        model.addAttribute("pageType", getLocalizedText("privacy.text"));
        return DISPLAY_TEXT_HTML;
    }

    // Support method
    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }
}
