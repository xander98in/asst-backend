package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.request;

import java.time.LocalDate;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para la actualización de un evaluador existente.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, EvaluatorUpdateRequestDTO.class})
@Schema(description = "Datos para actualizar un evaluador")
public class EvaluatorUpdateRequestDTO {

    @NotBlank(message = "{evaluator.fullName.notBlank}", groups = FirstGroup.class)
    @Size(max = 150, message = "{evaluator.fullName.size}", groups = SecondGroup.class)
    @Schema(description = "Nombre completo del evaluador", example = "Ana María Pérez")
    private String fullName;

    @NotBlank(message = "{evaluator.identificationNumber.notBlank}", groups = FirstGroup.class)
    @Size(max = 30, message = "{evaluator.identificationNumber.size}", groups = SecondGroup.class)
    @Schema(description = "Número de cédula del evaluador", example = "1061234567")
    private String identificationNumber;

    @NotBlank(message = "{evaluator.profession.notBlank}", groups = FirstGroup.class)
    @Size(max = 100, message = "{evaluator.profession.size}", groups = SecondGroup.class)
    @Schema(description = "Profesión del evaluador", example = "Psicóloga")
    private String profession;

    @Size(max = 150, message = "{evaluator.postgraduateDegree.size}", groups = SecondGroup.class)
    @Schema(description = "Postgrado del evaluador", example = "Esp. Seguridad y Salud en el Trabajo")
    private String postgraduateDegree;

    @NotBlank(message = "{evaluator.professionalCardNumber.notBlank}", groups = FirstGroup.class)
    @Size(max = 30, message = "{evaluator.professionalCardNumber.size}", groups = SecondGroup.class)
    @Schema(description = "Número de tarjeta profesional", example = "TP-12345")
    private String professionalCardNumber;

    @NotBlank(message = "{evaluator.occupationalHealthLicense.notBlank}", groups = FirstGroup.class)
    @Size(max = 30, message = "{evaluator.occupationalHealthLicense.size}", groups = SecondGroup.class)
    @Schema(description = "Número de licencia en salud ocupacional", example = "LIC-98765")
    private String occupationalHealthLicense;

    @NotNull(message = "{evaluator.licenseExpirationDate.notNull}", groups = FirstGroup.class)
    @Schema(description = "Fecha de expedición de la licencia", example = "2024-03-15")
    private LocalDate licenseExpirationDate;
}
