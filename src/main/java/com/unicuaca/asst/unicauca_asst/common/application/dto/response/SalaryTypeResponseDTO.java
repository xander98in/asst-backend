package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de los tipos de salario.
 * Incluye información sobre su identificación y nombre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryTypeResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Una parte fija y otra variable")
    private String name;
}
