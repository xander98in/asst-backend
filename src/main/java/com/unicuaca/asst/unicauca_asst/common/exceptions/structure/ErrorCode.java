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
     * Errores de infraestructura de base de datos y persistencia.
     * Solo para errores que el desarrollador no controla (BD caída, timeout, SQL inválido).
     * Estos errores se capturan en el RestApiExceptionHandler a partir de excepciones
     * lanzadas por Spring/Hibernate, no desde la lógica de dominio.
     */
    DATA_ERROR("ASST-DAT-0000", "Error de conflicto en datos."),
    DB_TIMEOUT("ASST-DAT-0001", "Tiempo de espera de base de datos."),
    DB_UNAVAILABLE("ASST-DAT-0002", "Servicio de datos no disponible."),
    DATA_ACCESS("ASST-DAT-0003", "Error accediendo a la base de datos."),
    SQL_GRAMMAR("ASST-DAT-0004", "Error de sintaxis SQL."),
    TRANSACTION_ERROR("ASST-DAT-0005", "Error de transacción."),
    /**
     * Errores relacionados con catálogos.
     */
    CATALOG_ERROR("ASST-CAT-0000", "Error genérico de catálogo."),
    CATALOG_EMPTY("ASST-CAT-0001", "El catálogo de %s está vacio. Debe existir al menos un registro."),
    CATALOG_UNAVAILABLE("ASST-CAT-0002", "Catálogo no disponible temporalmente."),
    /**
     * Errores de reglas de negocio — operaciones genéricas de dominio.
     * Se usan como plantilla reutilizable para cualquier entidad del sistema.
     * El contexto específico (qué entidad, qué ocurrió) se proporciona mediante
     * el message (técnico) y el userMessage (usuario final) de la excepción.
     */
    BUSINESS_RULE_VIOLATION("ASST-BUS-0001", "Regla de negocio violada"),
    ENTITY_NOT_FOUND("ASST-BUS-0002", "Entidad no encontrada. %s"),
    ENTITY_ALREADY_EXISTS("ASST-BUS-0003", "La entidad ya existe. %s"),
    ENTITY_CREATION_ERROR("ASST-BUS-0004", "Error al crear la entidad. %s"),
    ENTITY_UPDATE_ERROR("ASST-BUS-0005", "Error al actualizar la entidad. %s"),
    /**
     * Errores de reglas de negocio — específicos del dominio.
     */
    PERSON_WITH_BATTERY_RECORD("ASST-BUS-0006", "No se puede eliminar la persona evaluada, porque tiene registros de gestión de baterías asociados."),
    DELETE_BATTERY_MANAGEMENT_RECORD("ASST-BUS-0007", "No se puede eliminar el registro de gestión de batería en estado %s."),
    PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED("ASST-BUS-0008", "No es posible eliminar, porque el registro de gestión de batería ya tiene cuestionarios intralaborales (ILA/ILB) asociados."),
    EMPTY_LIST_OF_RESPONSES("ASST-BUS-0009", "No se han proporcionado respuestas para procesar."),
    DIFFERENT_RECORD_IDS_IN_RESPONSES("ASST-BUS-0010", "Se intentaron procesar respuestas pertenecientes a diferentes registros de gestión en un mismo lote."),
    QUESTION_DOES_NOT_BELONG_TO_QUESTIONNAIRE("ASST-BUS-0011", "La pregunta con ID %d no pertenece al cuestionario del registro actual."),
    QUESTION_ANSWERED_ALREADY("ASST-BUS-0012", "La pregunta con ID %d ya ha sido respondida para este registro."),
    DUPLICATE_QUESTION_IN_BATCH("ASST-BUS-0013", "La lista contiene preguntas duplicadas. No se puede responder la misma pregunta dos veces o más en el mismo lote."),
    RESPONSE_BELONGS_TO_OTHER_RECORD("ASST-BUS-0014", "La respuesta con ID %d no pertenece al registro de gestión de cuestionario ID %d."),
    RESPONSE_QUESTION_MISMATCH("ASST-BUS-0015", "El ID de pregunta proporcionado no coincide con el almacenado en la respuesta ID: %d"),
    QUESTIONNAIRE_RECORD_DELETE_NOT_ALLOWED("ASST-BUS-0016", "No se puede eliminar el registro de gestión de cuestionario porque se encuentra en estado '%s'."),
    CLOSE_BATTERY_MANAGEMENT_RECORD("ASST-BUS-0017", "No se puede cerrar el registro de gestión de batería en estado %s. Solo los registros en estado 'Diligenciado' pueden ser cerrados."),
    /**
     * Errores relacionados con mapeo y dependencias.
     */
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
