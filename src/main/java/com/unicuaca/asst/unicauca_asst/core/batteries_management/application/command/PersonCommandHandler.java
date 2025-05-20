package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;

public interface PersonCommandHandler {

    /**
     * Crea una nueva persona en el sistema a partir de los datos proporcionados en el DTO.
     *
     * @param dto objeto que contiene la información necesaria para crear una persona
     * @return una respuesta estándar {@link ApiResponse} que contiene un {@link PersonResponseDTO}
     *         con los datos de la persona recién creada, o un mensaje de error si falla la operación
     */
    ApiResponse<PersonResponseDTO> createPerson(PersonCreateRequestDTO dto);
}
