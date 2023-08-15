package com.revo.application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OneOfValidator implements ConstraintValidator<OneOf, Integer> {
    private List<Integer> valueList;

    @Override
    public void initialize(OneOf constraint) {
        var allowed = constraint.allowedValue();
        this.valueList = Arrays.stream(allowed)
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext context) {
        return s != null || this.valueList.contains(s);
    }
}
