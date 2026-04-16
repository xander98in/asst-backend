package com.unicuaca.asst.unicauca_asst.core.reports.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Modelo de dominio que representa a un evaluador profesional autorizado
 * para gestionar evaluaciones psicosociales en la plataforma.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Evaluator {

    private Long id;
    private String fullName;
    private String identificationNumber;
    private String profession;
    private String postgraduateDegree;
    private String professionalCardNumber;
    private String occupationalHealthLicense;
    private LocalDate licenseExpirationDate;
    private Long creatorUserId;
    private LocalDateTime createdAt;
}
