package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.dto.SubscriberDTO;
import com.project.EpicByte.model.entity.Subscriber;
import com.project.EpicByte.model.entity.productEntities.CartItem;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.SubscriberRepository;
import com.project.EpicByte.service.HomeService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

import static com.project.EpicByte.util.Constants.INDEX_HTML;
import static com.project.EpicByte.util.Constants.INDEX_URL;

@Service
public class HomeServiceImpl implements HomeService {
    private final SubscriberRepository subscriberRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    @Autowired
    public HomeServiceImpl(SubscriberRepository subscriberRepository,
                           ModelMapper modelMapper,
                           CartRepository cartRepository) {
        this.subscriberRepository = subscriberRepository;
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
    }

    @Override
    public String displayIndexPage(Model model, HttpSession session, Principal principal) {
        if (principal != null) {
            String username = principal.getName();

            // Fetch the cart item count from DB and save it in session on user initial login
            if (session.getAttribute("numItems") == null) {
                List<CartItem> cartItemList = this.cartRepository.findAllByUserUsername(username);
                session.setAttribute("numItems", cartItemList.size());
            }
        }

        model.addAttribute("subscriberDTO", new SubscriberDTO());
        return INDEX_HTML;
    }

    @Override
    public String subscribeHandler(SubscriberDTO subscriberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return INDEX_HTML;
        }

        this.subscriberRepository.save(modelMapper.map(subscriberDTO, Subscriber.class));
        return "redirect:" + INDEX_URL;
    }
}
