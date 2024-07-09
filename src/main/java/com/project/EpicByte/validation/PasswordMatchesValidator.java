package com.project.EpicByte.validation;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDTO> {
    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(final UserRegisterDTO user,
                           final ConstraintValidatorContext context) {
        // Handle the error here
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            context.disableDefaultConstraintViolation(); // disable the error on the global level.

            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("repeatPassword")
                    .addConstraintViolation(); // assign the error to repeatPassword field.
            return false;
        }

        return true;
    }
}