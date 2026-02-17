package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para consultas relacionadas con los registros de gestión de cuestionarios.
 */
public interface QuestionnaireManagementRecordQueryRepository {

    /**
     * Verifica si existen registros de gestión de cuestionarios asociados
     * a un registro de gestión de baterías específico y con abreviaciones
     * de cuestionarios dentro de una colección dada.
     *
     * @param batteryManagementRecordId ID del registro de gestión de baterías.
     * @param abbreviations             Colección de abreviaciones de cuestionarios.
     * @return {@code true} si existen registros que cumplen los criterios, {@code false} en caso contrario.
     */
    boolean existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(Long batteryManagementRecordId, Collection<String> abbreviations);

    /**
     * Verifica si existe al menos un registro de gestión de cuestionarios
     * asociado a un registro de gestión de baterías específico.
     *
     * @param batteryManagementRecordId ID del registro de gestión de baterías.
     * @return {@code true} si existe al menos un registro asociado, {@code false} en caso contrario.
     */
    boolean existsByBatteryManagementRecordId(Long batteryManagementRecordId);

    /**
     * Encuentra un registro de gestión de cuestionarios por el ID del registro de gestión de baterías
     * y la abreviación del cuestionario, incluyendo todos los detalles relacionados.
     *
     * @param batteryManagementRecordId ID del registro de gestión de baterías.
     * @param questionnaireAbbreviation Abreviación del cuestionario.
     * @return Un {@link Optional} que contiene el registro encontrado, o vacío si no existe.
     */
    Optional<QuestionnaireManagementRecord> findByBatteryManagementRecordIdAndQuestionnaireAbbreviationWithAll(
        Long batteryManagementRecordId,
        String questionnaireAbbreviation
    );

    /**
     * Obtiene un registro de gestión de cuestionario por su ID,
     * cargando todas sus relaciones (Batería, Cuestionario, Estado).
     *
     * @param id Identificador único del registro.
     * @return Optional con el modelo de dominio completo.
     */
    Optional<QuestionnaireManagementRecord> findByIdWithAll(Long id);

    /**
     * Verifica si existe un registro de gestión de cuestionario específico
     * para una batería y un cuestionario dados (por sus IDs).
     *
     * @param batteryId ID del registro de gestión de batería.
     * @param questionnaireId ID del cuestionario.
     * @return true si ya existe, false si no.
     */
    boolean existsByBatteryManagementRecordIdAndQuestionnaireId(Long batteryId, Long questionnaireId);

    /**
     * Obtiene la lista de abreviaturas de los cuestionarios asociados a una batería
     * que se encuentren en un estado específico (ej: "Diligenciado").
     *
     * @param batteryId ID del registro de gestión de batería.
     * @param statusName Nombre del estado a filtrar (ej: "Diligenciado").
     * @return Lista de strings con las abreviaturas (ej: ["ILA", "EXT"]).
     */
    List<String> findAbbreviationsByBatteryIdAndStatusName(Long batteryId, String statusName);
}
