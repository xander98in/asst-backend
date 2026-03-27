package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Catálogo de códigos de error de la aplicación.
 * Cada constante define un código único para trazabilidad y una clave de mensaje
 * técnica para ser resuelta mediante internacionalización (i18n).
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /** Errores genéricos del sistema */
    GENERIC_ERROR("ASST-GEN-0001", "tech.generic.error"),
    METHOD_NOT_ALLOWED("ASST-GEN-0002", "tech.generic.method_not_allowed"),
    UNSUPPORTED_MEDIA_TYPE("ASST-GEN-0003", "tech.generic.unsupported_media"),
    NOT_ACCEPTABLE("ASST-GEN-0004", "tech.generic.not_acceptable"),
    MAPPER_ERROR("ASST-0301", "tech.generic.mapper_error"),

    /** Errores relacionados con la validación de solicitudes */
    VALIDATION_ERROR("ASST-VAL-0000", "tech.validation.error"),
    BAD_REQUEST("ASST-VAL-0001", "tech.validation.bad_request"),
    FIELD_VALIDATION("ASST-VAL-0002", "tech.validation.fields"),

    /** Errores de autenticación y autorización */
    AUTHENTICATION_ERROR("ASST-SEC-0000", "tech.auth.error"),
    UNAUTHORIZED("ASST-SEC-0001", "tech.unauthorized"),
    FORBIDDEN("ASST-SEC-0002", "tech.forbidden"),
    INVALID_CREDENTIALS("ASST-SEC-0003", "tech.invalid_credentials"),
    USER_DISABLED("ASST-SEC-0004", "tech.user_disabled"),

    /** Errores de infraestructura de base de datos y persistencia */
    DATA_ERROR("ASST-DAT-0000", "tech.data.access_error"),
    DB_TIMEOUT("ASST-DAT-0001", "tech.data.timeout"),
    DB_UNAVAILABLE("ASST-DAT-0002", "tech.data.db_unavailable"),
    DATA_ACCESS("ASST-DAT-0003", "tech.data.access_error"),
    SQL_GRAMMAR("ASST-DAT-0004", "tech.data.sql_grammar"),
    TRANSACTION_ERROR("ASST-DAT-0005", "tech.data.transaction_error"),

    /** Errores relacionados con catálogos */
    CATALOG_ERROR("ASST-CAT-0000", "tech.catalog.error"),
    CATALOG_EMPTY("ASST-CAT-0001", "tech.catalog.empty"),

    /** Dominio Genérico - Operaciones de CRUD básicas */
    BUSINESS_RULE_VIOLATION("ASST-BUS-0001", "tech.business_rule_violation"),
    ENTITY_NOT_FOUND("ASST-BUS-0002", "tech.entity.not_found"),
    ENTITY_ALREADY_EXISTS("ASST-BUS-0003", "tech.entity.already_exists"),
    ENTITY_CREATION_ERROR("ASST-BUS-0004", "tech.entity.creation_error"),
    ENTITY_UPDATE_ERROR("ASST-BUS-0005", "tech.entity.update_error"),

    /** Dominio Específico (Baterías) - Lógica de negocio compleja */
    PERSON_WITH_BATTERY_RECORD("ASST-BUS-0006", "tech.person.has_records"),
    DELETE_BATTERY_MANAGEMENT_RECORD("ASST-BUS-0007", "tech.battery.delete_not_allowed"),
    PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED("ASST-BUS-0008", "tech.details.delete_not_allowed"),
    EMPTY_LIST_OF_RESPONSES("ASST-BUS-0009", "tech.responses.empty_list"),
    DIFFERENT_RECORD_IDS_IN_RESPONSES("ASST-BUS-0010", "tech.responses.inconsistent_batch"),
    QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE("ASST-BUS-0011", "tech.questionnaire.not_found"),
    QUESTION_ANSWERED_ALREADY("ASST-BUS-0012", "tech.responses.already_answered"),
    DUPLICATE_QUESTION_IN_BATCH("ASST-BUS-0013", "tech.responses.duplicate_questions"),
    RESPONSE_BELONGS_TO_OTHER_RECORD("ASST-BUS-0014", "tech.responses.security_error"),
    RESPONSE_QUESTION_MISMATCH("ASST-BUS-0015", "tech.responses.data_mismatch"),
    QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED("ASST-BUS-0016", "tech.questionnaire.delete_not_allowed"),
    CLOSE_BATTERY_MANAGEMENT_RECORD("ASST-BUS-0017", "tech.battery.close_not_allowed"),

    /** Dominio Específico - Persona Evaluada (No Encontrado / Ya Existe) */
    PERSON_NOT_FOUND("ASST-BUS-0018", "tech.person.not_found"),
    PERSON_ID_TYPE_NOT_FOUND("ASST-BUS-0019", "tech.person.id_type_not_found"),
    PERSON_STATUS_NOT_FOUND("ASST-BUS-0020", "tech.person.status_not_found"),
    PERSON_IDENTIFICATION_EXISTS("ASST-BUS-0031", "tech.person.identification_exists"),
    PERSON_EMAIL_EXISTS("ASST-BUS-0032", "tech.person.email_exists"),

    /** Dominio Específico - Gestión de Baterías (No Encontrado / Ya Existe) */
    BATTERY_RECORD_NOT_FOUND("ASST-BUS-0021", "tech.battery.record_not_found"),
    BATTERY_STATUS_NOT_FOUND("ASST-BUS-0022", "tech.battery.status_not_found"),
    BATTERY_RECORD_ALREADY_EXISTS("ASST-BUS-0033", "tech.battery.record_already_exists"),

    /** Dominio Específico - Cuestionarios (No Encontrado / Ya Existe) */
    QUESTIONNAIRE_NOT_FOUND_BY_REF("ASST-BUS-0023", "tech.questionnaire.not_found_by_ref"),
    QUESTIONNAIRE_MGMT_RECORD_NOT_FOUND("ASST-BUS-0024", "tech.questionnaire.record_not_found"),
    QUESTIONNAIRE_MGMT_STATUS_NOT_FOUND("ASST-BUS-0025", "tech.questionnaire.status_not_found"),
    QUESTIONNAIRE_ALREADY_ASSIGNED("ASST-BUS-0034", "tech.questionnaire.already_assigned"),

    /** Dominio Específico - Preguntas y Respuestas (No Encontrado) */
    QUESTION_NOT_FOUND("ASST-BUS-0026", "tech.question.not_found"),
    RESPONSE_NOT_FOUND("ASST-BUS-0028", "tech.response.not_found"),
    RESPONSE_OPTION_NOT_FOUND("ASST-BUS-0029", "tech.response.option_not_found"),

    /** Dominio Específico - Detalles Sociodemográficos (No Encontrado / Ya Existe) */
    PERSON_DETAILS_NOT_FOUND("ASST-BUS-0027", "tech.details.not_found"),
    PERSON_DETAILS_ALREADY_EXISTS("ASST-BUS-0035", "tech.details.already_exists"),

    /** Dominio Específico - Catálogos (No Encontrado) */
    CATALOG_RESOURCE_NOT_FOUND("ASST-BUS-0030", "tech.catalog.resource_not_found");

    /** Código único de error para trazabilidad. */
    private final String code;
    /** Clave de mensaje para resolución i18n (tech.*). */
    private final String messageKey;
}
