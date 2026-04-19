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

    /** Identificador del espacio de análisis al que pertenece la batería. */
    private Long analysisSpaceId;

    /** Identificador del registro de gestión de batería asociado al espacio. */
    private Long batteryManagementRecordId;

    /** Fecha y hora en que la batería fue agregada al espacio de análisis. */
    private LocalDateTime addedAt;
}
