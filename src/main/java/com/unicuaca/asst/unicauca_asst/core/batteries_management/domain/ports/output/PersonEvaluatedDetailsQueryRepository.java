package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;

import java.util.Optional;

/**
 * Puerto de salida para operaciones de consulta sobre los detalles de una persona evaluada
 * ({@link PersonEvaluatedDetails}).
 *
 * <p>Define las firmas que deben implementar los adaptadores de infraestructura encargados
 * de recuperar información desde fuentes externas (p. ej., base de datos relacional),
 * sin acoplar el dominio a los detalles de persistencia.</p>
 */
public interface PersonEvaluatedDetailsQueryRepository {

    /**
     * Verifica si ya existe un PersonEvaluatedDetails asociado al registro de gestión de batería dado (relación 1:1).
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return true si existe, false si no
     */
    boolean existsByBatteryManagementRecordId(Long batteryManagementRecordId);

    /**
     * Obtiene el nombre del área de trabajo por ID de registro de gestión de batería.
     *
     * @param recordId ID del registro de gestión de batería
     * @return Nombre del área (Optional vacío si no existe detalle asociado)
     */
    Optional<String> getWorkAreaNameByBatteryManagementRecordId(Long recordId);

    /**
     * Obtiene los detalles asociados al registro de gestión de batería.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return Optional con PersonEvaluatedDetails si existe
     */
    Optional<PersonEvaluatedDetails> getByBatteryManagementRecordId(Long batteryManagementRecordId);

    /**
     * Obtiene los detalles completos por ID del detalle (con relaciones cargadas).
     *
     * @param personEvaluatedDetailsId ID del detalle
     * @return Optional con PersonEvaluatedDetails si existe
     */
    Optional<PersonEvaluatedDetails> getByIdWithAll(Long personEvaluatedDetailsId);

    /**
     * Verifica si existe un PersonEvaluatedDetails por su ID.
     *
     * @param id ID del detalle
     * @return true si existe, false si no
     */
    boolean existsById(Long id);
}
