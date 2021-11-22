package com.ecdata.cmp.common.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author honglei
 * @since 2019-08-14
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";

    Class[] groups() default {};

    Class[] payload() default {};
}
