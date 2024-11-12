package com.example.employee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DOJValidator implements ConstraintValidator<ValidDOJ, LocalDate> {
    @Override
    public boolean isValid(LocalDate doj, ConstraintValidatorContext context) {
        return doj != null && doj.isBefore(LocalDate.now());
    }
}