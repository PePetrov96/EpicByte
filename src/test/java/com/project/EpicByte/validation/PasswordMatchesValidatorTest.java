package com.project.EpicByte.validation;

import com.project.EpicByte.model.dto.UserRegisterDTO;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class PasswordMatchesValidatorTest {
    private PasswordMatchesValidator validator;
    private UserRegisterDTO user;

    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        validator = new PasswordMatchesValidator();
        user = new UserRegisterDTO();
    }

    @Test
    void isValid_success_WhenPasswordsMatch() {
        user.setPassword("password");
        user.setRepeatPassword("password");

        Assertions.assertTrue(validator.isValid(user, context));

    }
}
