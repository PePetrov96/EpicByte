package com.project.EpicByte.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UniqueUpdateUsernameValidatorTest {
    private UniqueUpdateUsernameValidatorTest validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new UniqueUpdateUsernameValidatorTest();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }
}
