package com.siteminder.webmail.model.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailListValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailListConstraint {
    String message() default "Invalid email address format exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

