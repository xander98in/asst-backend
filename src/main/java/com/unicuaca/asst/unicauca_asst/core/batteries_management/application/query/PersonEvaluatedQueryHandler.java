package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import org.springframework.data.domain.Page;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedInformationListResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;

public interface PersonEvaluatedQueryHandler {

    /**
     * Consulta los datos de una persona a partir de su identificador único.
     *
     * @param idPersonEvaluated el ID de la persona que se desea consultar.
     * @return una {@link ApiResponse} que contiene un {@link PersonEvaluatedResponseDTO} con la información de la persona,
     *         o un mensaje de error y estado correspondiente si no se encuentra la persona.
     */
    PersonEvaluatedResponseDTO getPersonEvaluatedById(Long idPersonEvaluated);

    /**
     * Consulta personas evaluadas por tipo y número de identificación con paginación.
     *
     * @param abbreviation          la abreviatura del tipo de identificación
     * @param identificationNumber  el número de identificación
     * @param page                  el número de página (0-indexado)
     * @param size                  la cantidad de registros por página
     * @return una {@link ApiResponse} que contiene una lista paginada de {@link PersonEvaluatedInformationListResponseDTO}
     *         que coinciden con los criterios de búsqueda, o un mensaje de error si no se encuentran resultados.
     */
    Page<PersonEvaluatedInformationListResponseDTO> queryByIdentity(String abbreviation, String identificationNumber, Integer page, Integer size);

}
