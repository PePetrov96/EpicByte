package com.project.EpicByte.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class PastDateValidatorTest {
    private PastDateValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PastDateValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_fail_futureDate() {
        Assertions.assertFalse(validator.isValid(LocalDate.now().plusDays(1), context));
    }

    @Test
    public void isValid_fail_nullDate() {
        Assertions.assertFalse(validator.isValid(null, context));
    }

    @Test
    public void isValid_success_pastDate() {
        Assertions.assertTrue(validator.isValid(LocalDate.now().minusDays(1), context));
    }
}
