package com.seanco.bfcm.validator;

import com.seanco.bfcm.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<MobileValid, String> {
    private boolean required = false;

    @Override
    public void initialize(MobileValid constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isValidMobile(s);
        } else {
            if (StringUtils.isBlank(s)) {
                return true;
            }
            return ValidatorUtil.isValidMobile(s);
        }
    }
}
