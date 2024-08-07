package com.project.EpicByte.web;

import com.project.EpicByte.aop.SlowExecutionWarning;
import com.project.EpicByte.model.dto.SubscriberDTO;

import com.project.EpicByte.service.HomeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final HomeService homeService;
    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @SlowExecutionWarning
    @GetMapping("/")
    public String index(Model model,
                        HttpSession session,
                        Principal principal) {
        return this.homeService.displayIndexPage(model, session, principal);
    }

    @SlowExecutionWarning
    @PostMapping("/subscribe")
    public String subscribe(@Valid @ModelAttribute("subscriberDTO") SubscriberDTO subscriberDTO,
                            BindingResult bindingResult) {
        return this.homeService.subscribeHandler(subscriberDTO, bindingResult);
    }
}
