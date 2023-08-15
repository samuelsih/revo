package com.revo.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {
    private List<String> valueList;

    @Override
    public void initialize(OneOf constraint) {
        this.valueList = Arrays.asList(constraint.allowedValues());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return Objects.equals(s, "") || this.valueList.contains(s);
    }
}
