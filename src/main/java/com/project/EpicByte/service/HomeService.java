package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.SubscriberDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;

public interface HomeService {
    String displayIndexPage(Model model, HttpSession session, Principal principal);
    String subscribeHandler(SubscriberDTO subscriberDTO, BindingResult bindingResult);
}
