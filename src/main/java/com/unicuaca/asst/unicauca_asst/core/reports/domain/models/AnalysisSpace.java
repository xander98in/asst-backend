package com.unicuaca.asst.unicauca_asst.core.reports.domain.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un espacio de análisis donde el profesional agrupa
 * baterías cerradas para generar informes grupales.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class AnalysisSpace {

    /** Identificador único del espacio de análisis. */
    private Long id;

    /** Nombre descriptivo del espacio de análisis. */
    private String name;

    /** Identificador del evaluador asociado al espacio. */
    private Long evaluatorId;

    /** Identificador del usuario del sistema que creó el espacio. */
    private Long creatorUserId;

    /** Fecha y hora en que se creó el espacio de análisis. */
    private LocalDateTime createdAt;

    /** Lista de baterías cerradas agregadas al espacio para el análisis grupal. */
    private List<AnalysisSpaceBattery> batteries;
}
