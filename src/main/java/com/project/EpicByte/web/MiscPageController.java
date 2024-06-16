package com.project.EpicByte.web;

import com.project.EpicByte.model.dto.SubscriberDTO;
import com.project.EpicByte.model.entity.Subscriber;
import com.project.EpicByte.repository.SubscriberRepository;
import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.util.Breadcrumbs;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

import static com.project.EpicByte.util.Constants.*;

/**
 * The MiscPageController class is responsible for handling requests related to miscellaneous pages, such as
 * terms and conditions, privacy policy, and checkout confirmations.

 * While they break the Best-Practices approach, it would otherwise be unpractical
 * to extract these 3 GET requests in separate controllers, with respective services.
 */

@Controller
public class MiscPageController extends Breadcrumbs {
    private final MessageSource messageSource;
    private final SubscriberRepository subscriberRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MiscPageController(MessageSource messageSource, SubscriberRepository subscriberRepository, ModelMapper modelMapper) {
        this.messageSource = messageSource;
        this.subscriberRepository = subscriberRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("subscriberDTO", new SubscriberDTO());
        return INDEX_HTML;
    }

    @PostMapping("/subscribe")
    public String subscribe(@Valid @ModelAttribute("subscriberDTO") SubscriberDTO subscriberDTO,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return INDEX_HTML;
        }

        this.subscriberRepository.save(modelMapper.map(subscriberDTO, Subscriber.class));
        return "redirect:" + INDEX_URL;
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

    @GetMapping(USERS_CART_CHECKOUT_CONFIRM_URL)
    public String displayConfirmCheckoutPage(Model model) {
        addProductBreadcrumb(model, "/user/cart", "Cart", "Confirm Checkout");
        model.addAttribute("pageType", "Completed Successfully");
        model.addAttribute("pageText", getLocalizedText("order.successfully.received.text"));
        return DISPLAY_TEXT_HTML;
    }

    // Support method
    private String getLocalizedText(String text) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(text, null, locale);
    }
}
