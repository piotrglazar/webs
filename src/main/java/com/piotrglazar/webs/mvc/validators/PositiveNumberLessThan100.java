package com.piotrglazar.webs.mvc.validators;

import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConstraintComposition
@PositiveNumber
@Max(99)
@ReportAsSingleViolation
@Constraint(validatedBy = { })
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveNumberLessThan100 {

    public String message() default "Please provide a number between 0 and 99 inclusively";

    public Class<?>[] groups() default { };

    public Class<? extends Payload>[] payload() default { };
}
