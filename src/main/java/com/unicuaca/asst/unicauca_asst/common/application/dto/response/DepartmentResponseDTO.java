package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta que representa un departamento con sus ciudades.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO {

    @Schema(example = "5")
    private Long id;

    @Schema(example = "019")
    private String code;

    @Schema(example = "Cauca")
    private String name;

    /**
     * Ciudades del departamento, cada una sin su campo department.
     */
    private List<CitySummaryResponseDTO> cities;
}
