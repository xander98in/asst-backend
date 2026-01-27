package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEvaluatedDetailsMetaResponseDTO {

    @Schema(example = "10", description = "ID del detalle de persona evaluada")
    private Long id;

    @Schema(example = "15", description = "ID del registro de gestión de baterías asociado")
    private Long batteryManagementRecordId;

    @Schema(example = "2026-01-25T10:30:00", description = "Fecha de creación del detalle")
    private LocalDateTime createdAt;

    @Schema(example = "2026-01-26T08:10:00", description = "Fecha de última actualización del detalle")
    private LocalDateTime updatedAt;
}
