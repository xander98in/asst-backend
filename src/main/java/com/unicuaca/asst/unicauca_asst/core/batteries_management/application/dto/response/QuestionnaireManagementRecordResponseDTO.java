package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para representar un registro de gestión de cuestionario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireManagementRecordResponseDTO {

    @Schema(example = "20", description = "ID del registro de gestión de cuestionario")
    private Long id;

    @Schema(example = "2026-02-02T10:30:00", description = "Fecha de creación")
    private LocalDateTime createdAt;

    @Schema(example = "2026-02-03T08:10:00", description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    @Schema(example = "2", description = "ID del estado del registro de gestión de cuestionario")
    private Long statusId;

    @Schema(example = "Creado", description = "Nombre del estado del registro de gestión de cuestionario")
    private String statusName;

    @Schema(example = "15", description = "ID del registro de gestión de baterías asociado")
    private Long batteryManagementRecordId;

    @Schema(example = "1", description = "ID del cuestionario asociado")
    private Long questionnaireId;

    @Schema(example = "ILA", description = "Abreviatura del cuestionario")
    private String questionnaireAbbreviation;

    @Schema(example = "Cuestionario de Factores de Riesgo Psicosocial Intralaboral – Forma A", description = "Nombre del cuestionario")
    private String questionnaireName;
}
