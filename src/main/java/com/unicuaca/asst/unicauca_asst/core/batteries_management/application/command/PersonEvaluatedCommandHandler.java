package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;

/**
 * Manejador de comandos para operaciones sobre personas evaluadas.
 */
public interface PersonEvaluatedCommandHandler {

    /**
     * Registra una nueva persona evaluada en el sistema.
     *
     * @param dto datos de entrada
     * @return respuesta con la persona creada
     */
    PersonEvaluatedResponseDTO createPersonEvaluated(PersonEvaluatedCreateRequestDTO dto);

    /**
    * Actualiza una persona evaluada con los datos permitidos.
    *
    * @param id identificador de la persona
    * @param dto datos a actualizar
    * @return respuesta con la persona actualizada
    */
    PersonEvaluatedResponseDTO updatePersonEvaluated(Long id, PersonEvaluatedUpdateRequestDTO dto);
}
