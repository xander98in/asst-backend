package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar los datos de un tipo de contrato.
 * Incluye información como el ID y el nombre del tipo de contrato.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractTypeResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Término indefinido")
    private String name;
}
