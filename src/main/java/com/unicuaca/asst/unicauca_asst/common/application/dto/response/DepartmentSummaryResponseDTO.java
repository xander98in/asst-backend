package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que resume la información de un departamento.
 * Incluye el ID, código y nombre del departamento.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSummaryResponseDTO {

    @Schema(example = "5")
    private Long id;

    @Schema(example = "019")
    private String code;

    @Schema(example = "Cauca")
    private String name;
}
