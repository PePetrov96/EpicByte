package com.project.EpicByte.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUpdateUsernameValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUpdateUsername {
    String message() default "{username.already.exists.error.text}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
