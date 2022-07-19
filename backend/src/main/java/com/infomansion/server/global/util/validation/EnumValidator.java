package com.infomansion.server.global.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private ValidEnum annotaion;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotaion = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotaion.enumClass().getEnumConstants();
        if(enumValues != null) {
            for (Object enumValue : enumValues) {
                if(value.equals(enumValue.toString())
                        || (this.annotaion.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())))
                    return true;
            }
        }
        return false;
    }
}
