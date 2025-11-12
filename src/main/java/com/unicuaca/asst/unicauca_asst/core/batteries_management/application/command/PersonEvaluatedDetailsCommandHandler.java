package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
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
     * @return DTO de respuesta con la información creada y enriquecida desde el dominio
     */
    PersonEvaluatedDetailsResponseDTO createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto);
}
