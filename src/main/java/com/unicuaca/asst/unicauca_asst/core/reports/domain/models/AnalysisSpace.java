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

    private Long id;
    private String name;
    private Long evaluatorId;
    private Long creatorUserId;
    private LocalDateTime createdAt;
    private List<AnalysisSpaceBattery> batteries;
}
