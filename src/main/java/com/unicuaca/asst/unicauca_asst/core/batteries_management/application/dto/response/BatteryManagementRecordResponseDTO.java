package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO que representa la respuesta de un registro de gestión de baterías.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryManagementRecordResponseDTO {

    @Schema(example = "1", description = "ID único del registro de gestión de baterías")
    private Long id;

    @Schema(example = "2024-01-01T12:00:00", description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(example = "2024-01-02T12:00:00", description = "Fecha y hora de la última actualización del registro")
    private LocalDateTime updatedAt;

    @Schema(example = "Generado", description = "Estado actual del registro de gestión de baterías")
    private String status;

    @Schema(example = "1", description = "ID de la persona evaluada asociada al registro")
    private Long personEvaluatedId;

    @Schema(example = "Cédula de Ciudadanía", description = "Tipo de identificación de la persona evaluada")
    private String identificationType;

    @Schema(example = "CC", description = "Abreviatura del tipo de identificación")
    private String identificationAbbreviation;

    @Schema(example = "123456789", description = "Número de identificación de la persona evaluada")
    private String identificationNumber;

    @Schema(example = "Juan", description = "Primer nombre de la persona evaluada")
    private String firstName;

    @Schema(example = "Pérez", description = "Apellido de la persona evaluada")
    private String lastName;
}
