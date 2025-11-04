package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que representa una ciudad.
 * Incluye información básica de la ciudad y un resumen del departamento al que pertenece.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponseDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "001")
    private String code;

    @Schema(example = "Popayán")
    private String name;

    /**
     * Departamento al que pertenece la ciudad (sin ciudades).
     */
    private DepartmentSummaryResponseDTO department;
}
