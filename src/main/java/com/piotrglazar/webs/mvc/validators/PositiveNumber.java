package com.piotrglazar.webs.mvc.validators;

import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConstraintComposition
@Min(0)
@Digits(fraction = 0, integer = PositiveNumber.MAX_DIGITS)
@ReportAsSingleViolation
@Constraint(validatedBy = { })
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveNumber {

    int MAX_DIGITS = 10;

    String message() default "Please provide a non-negative number";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
