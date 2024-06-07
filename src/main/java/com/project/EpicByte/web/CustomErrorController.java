package com.project.EpicByte.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error-404")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorType", "Error 404");
                model.addAttribute("errorText", "This is not the Page you were looking for!");
                return "error-page";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorType", "Unauthorized");
                model.addAttribute("errorText", "This is not the Page you were looking for!");
                return "error-page";
            }
        }
        return "forward:/error-404";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        model.addAttribute("errorType", "Unauthorized");
        model.addAttribute("errorText", "This is not the Page you were looking for!");
        return "error-page";
    }
}