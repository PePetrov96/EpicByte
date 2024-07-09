package com.project.EpicByte.validation;

import com.project.EpicByte.repository.SubscriberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NotUniqueEmailValidator implements ConstraintValidator<NotUniqueEmail, String> {
    private final SubscriberRepository subscriberRepository;

    @Autowired
    public NotUniqueEmailValidator(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return this.subscriberRepository.findByEmail(s).isEmpty();
    }
}
