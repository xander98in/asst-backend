package com.unicuaca.asst.unicauca_asst.core.reports.domain.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa la relación entre un espacio de análisis y un registro
 * de gestión de batería cerrada.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class AnalysisSpaceBattery {

    private Long analysisSpaceId;
    private Long batteryManagementRecordId;
    private LocalDateTime addedAt;
}
