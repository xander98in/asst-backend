package com.unicuaca.asst.unicauca_asst.core.catalog.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta que resume la información de una ciudad sin incluir su departamento.
 *
 * <p>Se utiliza para representar ciudades embebidas dentro de respuestas de departamento,
 * evitando ciclos en la serialización.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitySummaryResponseDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "001")
    private String code;

    @Schema(example = "Popayán")
    private String name;
}
