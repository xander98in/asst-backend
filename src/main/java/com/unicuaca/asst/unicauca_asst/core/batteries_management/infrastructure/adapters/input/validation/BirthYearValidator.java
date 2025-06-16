package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.time.Year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthYearValidator implements ConstraintValidator<ValidBirthYear, Integer> {
    
    private static final int MAX_YEARS_BACK = 125;

    private int minYear;
    private int maxYear;

    @Override
    public void initialize(ValidBirthYear constraintAnnotation) {
        this.minYear = constraintAnnotation.min();
        this.maxYear = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return false; 

        int currentYear = Year.now().getValue();
        this.minYear = currentYear - MAX_YEARS_BACK;; 
        this.maxYear = currentYear; 
       
        if (value < this.minYear || value > this.maxYear) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "El año de nacimiento debe estar entre " + this.minYear + " y " + this.maxYear)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

