package com.unicuaca.asst.unicauca_asst.common.application.query;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

public interface CatalogQueryHandler {

    /**
     * Consulta los tipos de identificación disponibles en el sistema.
     *
     * @return una {@link ApiResponse} que contiene una lista de {@link IdentificationTypeResponseDTO} representando los tipos de identificación.
     */
    ApiResponse<List<IdentificationTypeResponseDTO>> getIdTypes();

}
