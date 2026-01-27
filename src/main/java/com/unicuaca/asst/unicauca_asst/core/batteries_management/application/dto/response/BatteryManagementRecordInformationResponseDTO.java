package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta que representa la información de un registro de gestión de baterías. (Util para listas)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryManagementRecordInformationResponseDTO {

    @Schema(example = "1", description = "ID único del registro de gestión de baterías")
    private Long id;

    @Schema(example = "2", description = "ID de la persona evaluada")
    private Long personEvaluatedId;

    @Schema(example = "Cédula de Ciudadania", description = "Tipo de identificación")
    private String identificationType;

    @Schema(example = "CC", description = "Abreviatura del tipo de identificación")
    private String identificationAbbreviation;

    @Schema(example = "123456789", description = "Número de identificación")
    private String identificationNumber;

    @Schema(example = "Juan", description = "Primer nombre")
    private String firstName;

    @Schema(example = "Pérez", description = "Apellido")
    private String lastName;

    @Schema(example = "Creado", description = "Estado del registro de gestión de baterías")
    private String batteryManagementRecordStatus;

    @Schema(example = "2026-01-25T10:30:00", description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    @Schema(example = "Talento Humano", description = "Área")
    private String area;
}
