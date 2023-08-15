package com.revo.application.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = OneOfValidator.class)
public @interface OneOf {
    String[] allowedValues();
    String message() default "invalid vote to choose";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
