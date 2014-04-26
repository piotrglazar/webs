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
@Digits(fraction = 0, integer = 10)
@ReportAsSingleViolation
@Constraint(validatedBy = { })
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveNumber {

    public String message() default "Please provide a non-negative number";

    public Class<?>[] groups() default { };

    public Class<? extends Payload>[] payload() default { };
}
