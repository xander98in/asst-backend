package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.time.Year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthYearValidator implements ConstraintValidator<ValidBirthYear, Integer> {
    private static final int MAX_YEARS_BACK = 125;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return false;
        int currentYear = Year.now().getValue();
        int minYear = currentYear - MAX_YEARS_BACK;

        return value >= minYear && value <= currentYear;
    }
}

