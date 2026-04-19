package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de la anotación {@link ValidIdentificationNumber} a nivel de clase.
 *
 * <p>Obtiene por reflexión los valores de los campos que contienen el número y el tipo
 * de identificación, y valida el formato del número aplicando la expresión regular
 * correspondiente al tipo (CC, CE, TI, PA, PEP, PPT).</p>
 */
public class IdentificationNumberValidator implements ConstraintValidator<ValidIdentificationNumber, Object> {

    private String fieldIdentificationNumber;
    private String fieldIdentificationType;

    /**
     * Inicializa el validador almacenando los nombres de los campos declarados en la anotación
     * para poder acceder a ellos por reflexión durante la validación.
     *
     * @param constraintAnnotation instancia de la anotación {@link ValidIdentificationNumber}
     */
    @Override
    public void initialize(ValidIdentificationNumber constraintAnnotation) {
        this.fieldIdentificationNumber = constraintAnnotation.fieldIdentificationNumber();
        this.fieldIdentificationType = constraintAnnotation.fieldIdentificationType();
    }

    /**
     * Valida el número de identificación del objeto evaluado según su tipo.
     *
     * @param value objeto a validar (normalmente un DTO con campos de identificación)
     * @param context contexto del validador usado para personalizar los mensajes de error
     * @return {@code true} si el número cumple el formato del tipo; {@code false} si no cumple,
     *         si alguno de los campos es {@code null} o si el tipo de identificación no está soportado
     */
    @Override
    public boolean isValid(Object value, jakarta.validation.ConstraintValidatorContext context) {

        try {
            String identificationNumber = (String) getFieldValue(value, fieldIdentificationNumber);
            String identificationType = (String) getFieldValue(value, fieldIdentificationType);

            context.disableDefaultConstraintViolation();
            if(identificationType == null) {
                context.buildConstraintViolationWithTemplate("El tipo de identificación no puede ser nulo")
                    .addPropertyNode(fieldIdentificationType)
                    .addConstraintViolation();
                return false;
            }
            if(identificationNumber == null) {
                context.buildConstraintViolationWithTemplate("El número de identificación no puede ser nulo")
                    .addPropertyNode(fieldIdentificationNumber)
                    .addConstraintViolation();
                return false;
            }

            return switch (identificationType) {
                // CC - Cédula de ciudadanía
                case "CC" -> validateField(identificationNumber, "\\d{6,10}", "La cédula de ciudadanía debe tener entre 6 y 10 dígitos", context);

                // CE - Cédula de extranjería
                case "CE" -> validateField(identificationNumber, "[A-Za-z0-9]{3,10}", "La cédula de extranjería debe tener entre 3 y 10 caracteres alfanuméricos", context);

                // TI - Tarjeta de identidad
                case "TI" -> validateField(identificationNumber, "\\d{10,11}", "La tarjeta de identidad debe tener entre 10 y 11 dígitos", context);

                // PA - Pasaporte
                case "PA" -> validateField(identificationNumber, "[A-Za-z0-9]{3,16}", "El pasaporte debe tener entre 3 y 16 caracteres alfanuméricos", context);

                // PEP - Permiso especial de permanencia
                case "PEP" -> validateField(identificationNumber, "[A-Za-z0-9]{15,15}", "El permiso especial de permanencia debe tener 15 caracteres alfanuméricos", context);

                // PPT - Permiso por protección temporal
                case "PPT" -> validateField(identificationNumber, "\\d{7,20}", "El permiso por protección temporal debe tener entre 7 y 20 dígitos", context);

                default -> {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("El número de identificación no es válido para el tipo de identificación seleccionado")
                        .addPropertyNode(fieldIdentificationNumber)
                        .addConstraintViolation();
                    yield false;
                }
            };
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica que el valor cumpla con la expresión regular indicada y, en caso contrario,
     * registra una violación personalizada sobre el campo del número de identificación.
     *
     * @param fieldValue valor del campo a validar
     * @param regex expresión regular que define el formato esperado
     * @param message mensaje de error a emitir si el valor no cumple la expresión
     * @param context contexto del validador
     * @return {@code true} si el valor cumple la expresión regular; {@code false} en caso contrario
     */
    private boolean validateField(String fieldValue, String regex, String message, ConstraintValidatorContext context) {
        if (fieldValue.matches(regex)) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(fieldIdentificationNumber)
            .addConstraintViolation();
        return false;
    }

    /**
     * Obtiene por reflexión el valor del campo indicado dentro del objeto recibido.
     *
     * @param object objeto del cual extraer el valor
     * @param fieldName nombre del campo declarado en la clase del objeto
     * @return valor del campo, o lanza excepción si no existe o no es accesible
     * @throws Exception si el campo no existe en la clase o no se puede acceder a él
     */
    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

}
