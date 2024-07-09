package com.project.EpicByte.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NotAdminUsernameValidatorTest {
    private NotAdminUsernameValidator validator;

    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new NotAdminUsernameValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(validator, "adminUsername", "admin");
    }

    @Test
    void isValid_fail_userNameIsAdmin() {
        Assertions.assertFalse(validator.isValid("admin", context));
    }

    @Test
    void isValid_success_userNameIsNotAdmin() {
        Assertions.assertTrue(validator.isValid("randomUsername", context));
    }
}