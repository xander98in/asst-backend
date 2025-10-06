package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.validation;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdentificationNumberValidator implements ConstraintValidator<ValidIdentificationNumber, Object> {

    private String fieldIdentificationNumber;
    private String fieldIdentificationType;

    @Override
    public void initialize(ValidIdentificationNumber constraintAnnotation) {
        this.fieldIdentificationNumber = constraintAnnotation.fieldIdentificationNumber();
        this.fieldIdentificationType = constraintAnnotation.fieldIdentificationType();
    }

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

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

}
