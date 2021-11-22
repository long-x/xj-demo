package com.ecdata.cmp.common.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author honglei
 * @since 2019-08-14
 */
@Documented
@Constraint(validatedBy = TimezoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timezone {
    String message() default "Invalid timezone";
    Class[] groups() default {};
    Class[] payload() default {};
}
