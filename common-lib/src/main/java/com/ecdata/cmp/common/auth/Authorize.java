package com.ecdata.cmp.common.auth;

import java.lang.annotation.*;

/**
 * @author honglei
 * @since 2019-08-16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorize {
    // allowed consumers
    String[] value();
}
