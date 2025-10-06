package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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
