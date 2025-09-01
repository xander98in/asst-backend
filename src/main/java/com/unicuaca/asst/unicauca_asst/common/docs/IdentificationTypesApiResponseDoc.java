package com.unicuaca.asst.unicauca_asst.common.docs;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "IdentificationTypesApiResponse")
public class IdentificationTypesApiResponseDoc {

    @Schema(example = "200")
    public int httpStatus;

    @Schema(example = "Tipos de identificación obtenidos exitosamente")
    public String message;

    @Schema(description = "Lista de tipos de identificación")
    public List<IdentificationTypeResponseDTO> data;

}
