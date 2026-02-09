package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;

/**
 * Manejador de comandos para operaciones de creación/modificación
 * sobre los detalles adicionales de una persona evaluada (PersonEvaluatedDetails).
 */
public interface PersonEvaluatedDetailsCommandHandler {

    /**
     * Registra los detalles de una persona evaluada en el sistema.
     *
     * @param dto datos de entrada validados para la creación
     */
    void createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto);

    /**
     * Actualiza los detalles de una persona evaluada en el sistema.
     *
     * @param personEvaluatedDetailsId ID del registro a actualizar
     * @param dto datos de entrada validados para la actualizacióN
     */
    void updatePersonEvaluatedDetails(Long personEvaluatedDetailsId, PersonEvaluatedDetailsUpdateRequestDTO dto);

    /**
     * Elimina los detalles de una persona evaluada del sistema.
     *
     * @param personEvaluatedDetailsId ID del registro a eliminar
     */
    void deletePersonEvaluatedDetails(Long personEvaluatedDetailsId);
}
