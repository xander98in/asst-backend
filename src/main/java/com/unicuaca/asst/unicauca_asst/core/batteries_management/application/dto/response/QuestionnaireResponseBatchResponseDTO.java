package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta que agrupa la información del registro de gestión de cuestionario
 * y la lista completa de sus respuestas asociadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireResponseBatchResponseDTO {

    // --- Información del Registro de Gestión (Padre) ---
    @Schema(description = "ID del registro de gestión de cuestionario", example = "123")
    private Long id;

    @Schema(description = "ID del registro de gestión de batería asociado", example = "45")
    private Long batteryManagementRecordId;

    // --- Información del Estado ---
    @Schema(description = "ID del estado del registro", example = "3")
    private Long statusId;

    @Schema(description = "Nombre del estado del registro", example = "Diligenciado")
    private String statusName;

    // --- Información del Cuestionario ---
    @Schema(description = "ID del cuestionario", example = "1")
    private Long questionnaireId;

    @Schema(description = "Nombre del cuestionario", example = "Cuestionario Intralaboral Forma A")
    private String questionnaireName;

    @Schema(description = "Abreviatura del cuestionario", example = "ILA")
    private String questionnaireAbbreviation;

    @Schema(description = "Descripción del cuestionario", example = "Evalúa condiciones intralaborales...")
    private String questionnaireDescription;

    // --- Auditoría ---
    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización del registro")
    private LocalDateTime updatedAt;

    // --- Lista de Respuestas (Hijos) ---
    @Schema(description = "Lista de respuestas asociadas a este registro")
    private List<QuestionnaireAnswerResponseDTO> answers;
}
