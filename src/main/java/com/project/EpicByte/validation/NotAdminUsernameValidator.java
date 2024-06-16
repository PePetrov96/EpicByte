package com.project.EpicByte.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class NotAdminUsernameValidator implements ConstraintValidator<NotAdminUsername, String> {
    @Value("${admin.username}")
    private String adminUsername;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !username.equalsIgnoreCase(adminUsername);
    }
}
