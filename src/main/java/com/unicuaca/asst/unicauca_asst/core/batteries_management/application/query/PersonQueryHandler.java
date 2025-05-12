package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;

public interface PersonQueryHandler {

    /**
     * Consulta los datos de una persona a partir de su identificador único.
     *
     * @param idPerson el ID de la persona que se desea consultar.
     * @return una {@link ApiResponse} que contiene un {@link PersonResponseDTO} con la información de la persona,
     *         o un mensaje de error y estado correspondiente si no se encuentra la persona.
     */
    ApiResponse<PersonResponseDTO> getPersonById(Long idPerson);

}
