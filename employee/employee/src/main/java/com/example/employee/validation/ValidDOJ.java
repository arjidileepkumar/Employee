package com.example.employee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DOJValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDOJ {
    String message() default "Date of joining must be a past date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}