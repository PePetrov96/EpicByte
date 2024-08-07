package com.project.EpicByte.web;

import com.project.EpicByte.aop.SlowExecutionWarning;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@Controller
public class MyErrorController implements ErrorController {
    private final MessageSource messageSource;
    private LocaleContextHolder localeContextHolder;

    @Autowired
    public MyErrorController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @SlowExecutionWarning
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,
                              WebRequest webRequest) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            Locale locale = LocaleContextHolder.getLocale();

            if (statusCode == 403) {
                webRequest.setAttribute("errorType", getLocalizedMessage("error.403", locale),
                        WebRequest.SCOPE_REQUEST);
                webRequest.setAttribute("errorText",
                        getLocalizedMessage("error.403.text", locale), WebRequest.SCOPE_REQUEST);
                return "error";
            } else if (statusCode == 404) {
                webRequest.setAttribute("errorType", getLocalizedMessage("error.404", locale),
                        WebRequest.SCOPE_REQUEST);
                webRequest.setAttribute("errorText", getLocalizedMessage("error.404.text", locale),
                        WebRequest.SCOPE_REQUEST);
                return "error";
            } else if (statusCode == 500) {
                webRequest.setAttribute("errorType", getLocalizedMessage("error.500", locale),
                        WebRequest.SCOPE_REQUEST);
                webRequest.setAttribute("errorText", getLocalizedMessage("error.500.text", locale),
                        WebRequest.SCOPE_REQUEST);
                return "error";
            } else {
                webRequest.setAttribute("errorType", getLocalizedMessage("error.unknown", locale),
                        WebRequest.SCOPE_REQUEST);
                webRequest.setAttribute("errorText", getLocalizedMessage("error.unknown.text", locale),
                        WebRequest.SCOPE_REQUEST);
                return "error";
            }

        }
        return "error";
    }

    private String getLocalizedMessage(String messageName, Locale locale) {
        try {
            return messageSource.getMessage(messageName, null, locale);
        } catch (NoSuchMessageException e) {
            return "Unresolved key: " + messageName;
        }
    }
}