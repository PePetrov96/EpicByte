package com.project.EpicByte.web;

import com.project.EpicByte.exceptions.NoSuchProductException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.project.EpicByte.util.Constants.ERROR_PAGE_HTML;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle errors when a UUID is manually input it is NOT a valid UUID format
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public String handleInvalidUUIDFormat(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("errorType", "Oops...");
        model.addAttribute("errorText", "Such product does not exist");
        return ERROR_PAGE_HTML;
    }

    // Handle errors when a product was removed and/or no longer exists
    @ExceptionHandler(NoSuchProductException.class)
    @ResponseStatus(HttpStatus.OK)
    public String handleNoSuchProduct(NoSuchProductException ex, Model model) {
        model.addAttribute("errorType", "Oops...");
        model.addAttribute("errorText", ex.getMessage());
        return ERROR_PAGE_HTML;
    }
}
