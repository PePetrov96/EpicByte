package com.project.EpicByte.validation;

import com.project.EpicByte.model.entity.Subscriber;
import com.project.EpicByte.repository.SubscriberRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotUniqueEmailValidatorTest {
    private NotUniqueEmailValidator validator;

    private ConstraintValidatorContext context;

    @Mock
    SubscriberRepository subscriberRepository;

    @BeforeEach
    void setUp() {
        validator = new NotUniqueEmailValidator(subscriberRepository);
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_fail_userAlreadySubscribed() {
        when(subscriberRepository.findByEmail(any())).thenReturn(Optional.of(new Subscriber()));
        Assertions.assertFalse(validator.isValid("randomEmail", context));
    }

    @Test
    void isValid_success_userNotASubscriber() {
        when(subscriberRepository.findByEmail(any())).thenReturn(Optional.empty());
        Assertions.assertTrue(validator.isValid("", context));
    }
}
