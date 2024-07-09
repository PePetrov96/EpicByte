package com.project.EpicByte.validation;

import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.repository.UserRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniqueUsernameValidatorTest {
    private UniqueUsernameValidator validator;
    private ConstraintValidatorContext context;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        validator = new UniqueUsernameValidator(userRepository);
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @Test
    public void isValid_fail_usernameAlreadyExists() {
        when(userRepository.existsByUsername(any())).thenReturn(true);
        Assertions.assertFalse(validator.isValid("username", context));
    }

    @Test
    public void isValid_success_uniqueUsername() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        Assertions.assertTrue(validator.isValid("username", context));
    }
}
