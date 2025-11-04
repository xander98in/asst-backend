package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;

/**
 * Manejador de comandos para operaciones sobre los detalles de una persona evaluada.
 */
public interface PersonEvaluatedDetailsCommandHandler {

    /**
     * Crea los detalles de una persona evaluada.
     *
     * @param dto datos de entrada validados
     * @return DTO de respuesta con la información creada
     */
    PersonEvaluatedDetailsResponseDTO createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto);
}
