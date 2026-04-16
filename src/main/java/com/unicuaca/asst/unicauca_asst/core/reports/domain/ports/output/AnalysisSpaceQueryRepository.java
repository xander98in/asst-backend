package com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.AnalysisSpace;

/**
 * Puerto de salida para operaciones de lectura sobre espacios de análisis.
 */
public interface AnalysisSpaceQueryRepository {

    /**
     * Lista todos los espacios de análisis creados por un usuario.
     *
     * @param userId ID del usuario creador
     * @return lista de espacios de análisis
     */
    List<AnalysisSpace> findAllByCreatorUserId(Long userId);

    /**
     * Obtiene un espacio de análisis con sus baterías asociadas.
     *
     * @param spaceId ID del espacio de análisis
     * @return Optional con el espacio encontrado
     */
    Optional<AnalysisSpace> findByIdWithBatteries(Long spaceId);

    /**
     * Verifica si existe un espacio de análisis por su ID.
     *
     * @param spaceId ID del espacio de análisis
     * @return true si existe
     */
    boolean existsById(Long spaceId);

    /**
     * Verifica si una batería ya está asociada a un espacio de análisis.
     *
     * @param spaceId             ID del espacio de análisis
     * @param batteryRecordId     ID del registro de batería
     * @return true si la batería ya está en el espacio
     */
    boolean existsBatteryInSpace(Long spaceId, Long batteryRecordId);
}
