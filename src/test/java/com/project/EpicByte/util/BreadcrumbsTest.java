package com.project.EpicByte.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BreadcrumbsTest {
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private Breadcrumbs breadcrumbs;

    @Mock
    private Model model;

    @Test
    void addProductBreadcrumb() {
        String pageUrl = "testUrl";
        String pageName = "testPage";
        when(messageSource.getMessage(any(), any(), any())).thenReturn("testMessage");
        breadcrumbs.addProductBreadcrumb(model, pageUrl, pageName);
        verify(model, times(1)).addAttribute(eq("breadcrumbs"), any(Map.class));
    }

    @Test
    void getPageText() {
        when(messageSource.getMessage(anyString() + ".text", any(), any())).thenReturn("testMessage");
        String result = breadcrumbs.getPageText("Test Page");
        assertEquals("testMessage", result);
    }
}