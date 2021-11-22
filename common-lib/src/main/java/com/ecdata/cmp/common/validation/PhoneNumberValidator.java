package com.ecdata.cmp.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author honglei
 * @since 2019-08-14
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            //can be null
            return true;
        }
        final int min = 8;
        final int max = 14;
        return phoneField != null && phoneField.matches("[0-9]+") && (phoneField.length() > min) && (phoneField.length() < max);
    }
}
