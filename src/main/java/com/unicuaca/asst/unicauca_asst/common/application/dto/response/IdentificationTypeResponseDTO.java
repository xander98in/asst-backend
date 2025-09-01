package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO de respuesta para representar los datos de un tipo de identificación.
 * Incluye el ID, la descripción y la abreviatura del tipo de identificación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentificationTypeResponseDTO {

    @Schema(example = "1")
    private Long idIdentificationType;

    @Schema(example = "Cédula de Ciudadanía")
    private String description;

    @Schema(example = "CC")
    private String abbreviation;

}
