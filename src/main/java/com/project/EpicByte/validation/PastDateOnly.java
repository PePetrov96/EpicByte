package com.project.EpicByte.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastDateValidator.class)
public @interface PastDateOnly {
    String message() default "{product.publicationDate.empty.error.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
