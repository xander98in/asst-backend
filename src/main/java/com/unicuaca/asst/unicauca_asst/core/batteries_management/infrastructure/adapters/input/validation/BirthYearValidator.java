package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.time.Year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de la anotación {@link ValidBirthYear} para años de nacimiento.
 *
 * <p>Aplica un rango dinámico derivado del año actual: el máximo es el año en curso
 * y el mínimo es el año actual menos {@value #MAX_YEARS_BACK}, garantizando que
 * el valor corresponda a una persona con una edad razonable.</p>
 */
public class BirthYearValidator implements ConstraintValidator<ValidBirthYear, Integer> {

    private static final int MAX_YEARS_BACK = 125;

    private int minYear;
    private int maxYear;

    /**
     * Inicializa el validador a partir de la anotación, tomando los valores por defecto
     * {@code min} y {@code max} declarados. Estos se recalculan dinámicamente en {@link #isValid}.
     *
     * @param constraintAnnotation instancia de la anotación {@link ValidBirthYear}
     */
    @Override
    public void initialize(ValidBirthYear constraintAnnotation) {
        this.minYear = constraintAnnotation.min();
        this.maxYear = constraintAnnotation.max();
    }

    /**
     * Valida que el año proporcionado esté dentro del rango permitido, calculado de forma
     * dinámica a partir del año actual.
     *
     * @param value valor entero del año a validar
     * @param context contexto del validador usado para personalizar el mensaje de error
     * @return {@code true} si el valor es un año válido; {@code false} en caso contrario o si es {@code null}
     */
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

