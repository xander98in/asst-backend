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

    /** ID del espacio de análisis (coincide con el campo {@code analysisSpace} de la entidad). */
    private Long analysisSpace;

    /** ID del registro de gestión de batería asociado al espacio. */
    private Long batteryManagementRecordId;
}
