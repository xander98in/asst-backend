package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * Errores genéricos del sistema.
     */
    GENERIC_ERROR("ASST-GEN-0001", "Error interno del servidor."),
    METHOD_NOT_ALLOWED("ASST-GEN-0002", "Método HTTP no permitido."),
    UNSUPPORTED_MEDIA_TYPE("ASST-GEN-0003", "Tipo de contenido no soportado."),
    NOT_ACCEPTABLE("ASST-GEN-0004", "Formato de respuesta no aceptable."),
    /**
     * Errores relacionados con la validación de solicitudes.
     */
    VALIDATION_ERROR("ASST-VAL-0000", "Error genérico de validación."),
    BAD_REQUEST("ASST-VAL-0001", "Solicitud inválida."),
    FIELD_VALIDATION("ASST-VAL-0002", "Hay errores de validación en los campos."),
    /**
     * Errores de autenticación y autorización.
     */
    AUTHENTICATION_ERROR("ASST-SEC-0000", "Error genérico de autenticación."),
    UNAUTHORIZED("ASST-SEC-0001", "No autenticado."),
    FORBIDDEN("ASST-SEC-0002", "Acceso denegado."),
    INVALID_CREDENTIALS("ASST-SEC-0003", "Credenciales inválidas."),
    USER_DISABLED("ASST-SEC-0004", "El usuario no ha sido verificado."),
    /**
     * Errores relacionados con la base de datos y persistencia.
     */
    DATA_ERROR("ASST-DAT-0000", "Error de conflicto en datos."),
    DB_TIMEOUT("ASST-DAT-0001", "Tiempo de espera de base de datos."),
    DB_UNAVAILABLE("ASST-DAT-0002", "Servicio de datos no disponible."),
    DATA_ACCESS("ASST-DAT-0003", "Error accediendo a la base de datos."),
    SQL_GRAMMAR("ASST-DAT-0004", "Error de sintaxis SQL."),
    TRANSACTION_ERROR("ASST-DAT-0005", "Error de transacción."),
    ENTITY_ALREADY_EXISTS("ASST-DAT-0006", "La entidad ya existe. %s"),
    ENTITY_NOT_FOUND("ASST-DAT-0007", "Entidad no encontrada. %s"),
    ENTITY_CREATION_ERROR("ASST-DAT-0008", "Error al crear la entidad. %s"),
    ENTITY_UPDATE_ERROR("ASST-DAT-0009", "Error al actualizar la entidad. %s"),
    /**
     * Errores relacionados con catálogos.
     */
    CATALOG_ERROR("ASST-CAT-0000", "Error genérico de catálogo."),
    CATALOG_EMPTY("ASST-CAT-0001", "El catálogo de %s está vacio. Debe existir al menos un registro."),
    CATALOG_UNAVAILABLE("ASST-CAT-0002", "Catálogo no disponible temporalmente."),
    /**
     * Errores específicos de reglas de negocio.
     */
    BUSINESS_RULE_VIOLATION("ASST-BUS-0001", "Regla de negocio violada"),
    STATE_NOT_FOUND("ASST-BUS-0002", "El estado %s no fue encontrado."),
    PERSON_EVALUATED_NOT_FOUND("ASST-BUS-0003", "La persona evaluada con ID %s no fue encontrada."),
    BATTERY_RECORD_DUPLICATE("ASST-BUS-0004", "La persona evaluada con ID %s ya tiene un registro de gestión de baterías."),
    EMAIL_ALREADY_EXISTS("ASST-BUS-0005", "Correo ya registrado. %s"),

    MAPPER_ERROR("ASST-0301", "Error de mapeo o dependencia no disponible");

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
