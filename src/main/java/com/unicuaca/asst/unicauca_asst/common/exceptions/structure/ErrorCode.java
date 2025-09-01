package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * Error genérico no clasificado.
     */
    GENERIC_ERROR("ASST-0001", "ERROR GENERICO"),
    /**
     * La entidad que se intenta crear ya existe.
     */
    ENTITY_ALREADY_EXISTS("ASST-0002", "ERROR ENTIDAD YA EXISTE"),
    /**
     * La entidad solicitada no fue encontrada.
     */
    ENTITY_NOT_FOUND("ASST-0003", "Entidad no encontrada"),
    /**
     * Se ha violado una regla de negocio definida por el sistema.
     */
    BUSINESS_RULE_VIOLATION("ASST-0004", "Regla de negocio violada"),
    /**
     * Error al autenticar, credenciales inválidas.
     */
    INVALID_CREDENTIALS("ASST-0005", "Error al iniciar sesión, compruebe sus credenciales y vuelva a intentarlo"),
    /**
     * El correo electrónico ya está registrado en el sistema.
     */
    EMAIL_ALREADY_EXISTS("ASST-0006", "Correo ya registrado"),
    /**
     * El usuario existe pero no está habilitado o verificado.
     */
    USER_DISABLED("ASST-0007", "El usuario no ha sido verificado, por favor revise su correo para verificar su cuenta"),
    /**
     * Error al intentar crear una entidad, pero la operación falló inesperadamente.
     */
    ENTITY_CREATION_ERROR("ASST-0008", "Error al crear la entidad"),
    
    CATALOG_EMPTY("ASST-1001", "Catálogo vacío"),
    CATALOG_UNAVAILABLE("ASST-1002", "Catálogo no disponible"),
    
    DB_TIMEOUT("ASST-0101", "Tiempo de espera de base de datos"),
    DB_UNAVAILABLE("ASST-0102", "Servicio de datos no disponible"),
    DATA_ACCESS_ERROR("ASST-0103", "Error accediendo a la base de datos"),
    SQL_GRAMMAR_ERROR("ASST-0104", "Error de sintaxis o estructura SQL"),
    TRANSACTION_ERROR("ASST-0105", "Error de transacción"),

    BAD_REQUEST("ASST-0200", "Solicitud inválida"),
    METHOD_NOT_ALLOWED("ASST-0205", "Método HTTP no permitido"),
    UNSUPPORTED_MEDIA_TYPE("ASST-0215", "Tipo de contenido no soportado"),
    NOT_ACCEPTABLE("ASST-0206", "Formato de respuesta no aceptable"),
    UNAUTHORIZED("ASST-0401", "No autenticado"),
    FORBIDDEN("ASST-0403", "Acceso denegado"),

    MAPPER_ERROR("ASST-0301", "Error de mapeo o dependencia no disponible"),
    VALIDATION_ERROR("ASST-0201", "Error de validación de campos");

    /**
     * Código único del error (para trazabilidad y estandarización).
     */
    private final String code;

    /**
     * Mensaje clave o descripción estándar del error.
     * Puede ser traducido o utilizado como plantilla de respuesta.
     */
    private final String messageKey;
}
