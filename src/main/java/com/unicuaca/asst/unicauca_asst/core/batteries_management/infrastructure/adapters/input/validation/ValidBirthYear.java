package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Anotación de validación que restringe un valor de año de nacimiento a un rango permitido.
 *
 * <p>Delega la validación en {@link BirthYearValidator}, que calcula dinámicamente el rango
 * a partir del año actual (hasta 125 años hacia atrás).</p>
 */
@Constraint(validatedBy = BirthYearValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface ValidBirthYear {

    String message() default "El año de nacimiento debe estar entre {min} y {max}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int min() default 0;
    int max() default 0;
}
