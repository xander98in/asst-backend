package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar el estado del registro de gestión del cuestionario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireManagementRecordStatusResponseDTO {

    @Schema(example = "1", description = "ID único del estado del registro de gestión del cuestionario")
    private Long id;

    @Schema(example = "Cerrado", description = "Nombre del estado del registro de gestión del cuestionario") 
    private String name;
}
