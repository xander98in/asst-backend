package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Anotación de validación a nivel de clase que verifica la coherencia entre el número
 * y el tipo de identificación (CC, CE, TI, PA, PEP, PPT).
 *
 * <p>Delega la validación en {@link IdentificationNumberValidator}, que aplica la expresión
 * regular correspondiente al tipo sobre el número recibido.</p>
 */
@Constraint(validatedBy = IdentificationNumberValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentificationNumber {

    String message() default "El número de identificación no es válido para el tipo de identificación seleccionado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    String fieldIdentificationNumber() default "identificationNumber";
    String fieldIdentificationType() default "identificationTypeId";
}
