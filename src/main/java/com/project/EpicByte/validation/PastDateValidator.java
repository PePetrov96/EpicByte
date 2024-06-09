package com.project.EpicByte.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class PastDateValidator implements ConstraintValidator<PastDateOnly, LocalDate> {

    @Override
    public void initialize(PastDateOnly constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && !localDate.isAfter(LocalDate.now());
    }
}
