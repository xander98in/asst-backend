package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para una batería asociada a un espacio de análisis.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisSpaceBatteryResponseDTO {

    /** Identificador del registro de gestión de batería asociado al espacio. */
    @Schema(description = "ID del registro de gestión de batería", example = "1")
    private Long batteryManagementRecordId;

    /** Fecha y hora en que la batería fue agregada al espacio de análisis. */
    @Schema(description = "Fecha y hora en que se agregó la batería al espacio", example = "2025-05-15T10:30:00")
    private LocalDateTime addedAt;
}
