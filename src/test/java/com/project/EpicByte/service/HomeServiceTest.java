package com.project.EpicByte.service;

import com.project.EpicByte.model.dto.SubscriberDTO;
import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.CartRepository;
import com.project.EpicByte.repository.SubscriberRepository;
import com.project.EpicByte.service.impl.HomeServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;

import static com.project.EpicByte.util.Constants.INDEX_HTML;
import static com.project.EpicByte.util.Constants.INDEX_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeServiceTest {
    @Mock
    SubscriberRepository subscriberRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    CartRepository cartRepository;

    @Mock
    Model model;

    @Mock
    HttpSession session;

    @Mock
    Principal principal;

    HomeService homeService;
    BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        this.model = Mockito.mock(Model.class);
        this.session = Mockito.mock(HttpSession.class);
        this.principal = Mockito.mock(Principal.class);
        this.bindingResult = mock(BindingResult.class);

        homeService = new HomeServiceImpl(subscriberRepository,
                modelMapper,
                cartRepository);
    }

    @Test
    public void displayIndexPage_success() {
        when(this.cartRepository.findAllByUserUsername(any())).thenReturn(new ArrayList<>());

        String result = this.homeService.displayIndexPage(model, session, principal);
        Assertions.assertEquals(INDEX_HTML, result);
    }

    @Test
    public void subscribeHandler_success() {
        String result = this.homeService.subscribeHandler(new SubscriberDTO(), bindingResult);
        Assertions.assertEquals("redirect:" + INDEX_URL, result);
    }

    @Test
    public void subscribeHandler_fail_bindingResultHasErrors() {
        when(this.bindingResult.hasErrors()).thenReturn(true);
        String result = this.homeService.subscribeHandler(new SubscriberDTO(), bindingResult);
        Assertions.assertEquals(INDEX_HTML, result);
    }
}
