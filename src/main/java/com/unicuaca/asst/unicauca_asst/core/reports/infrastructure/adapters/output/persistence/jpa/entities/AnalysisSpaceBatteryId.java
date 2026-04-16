package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Clase de clave primaria compuesta para {@link AnalysisSpaceBatteryEntity}.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AnalysisSpaceBatteryId implements Serializable {

    private Long analysisSpace;
    private Long batteryManagementRecordId;
}
