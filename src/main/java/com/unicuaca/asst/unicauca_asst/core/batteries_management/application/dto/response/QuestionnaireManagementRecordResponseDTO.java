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

    @Schema(description = "ID único del registro de gestión de cuestionario", example = "1")
    private Long id;

    @Schema(description = "ID del registro de gestión de baterías asociado", example = "100")
    private Long batteryManagementRecordId;

    @Schema(description = "ID del estado del registro", example = "5")
    private Long statusId;

    @Schema(description = "Nombre del estado del registro", example = "CREADO")
    private String statusName;

    @Schema(description = "ID del cuestionario asociado", example = "50")
    private Long questionnaireId;

    @Schema(description = "Nombre del cuestionario", example = "Cuestionario de Estrés")
    private String questionnaireName;

    @Schema(description = "Abreviatura del cuestionario", example = "EST")
    private String questionnaireAbbreviation;

    @Schema(description = "Descripción del cuestionario", example = "Evalúa nivel de estrés...")
    private String questionnaireDescription;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
