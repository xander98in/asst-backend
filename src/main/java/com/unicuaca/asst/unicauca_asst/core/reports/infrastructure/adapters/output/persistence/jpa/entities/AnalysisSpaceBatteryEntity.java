package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA para la relación entre un espacio de análisis y un registro
 * de gestión de batería. Clave primaria compuesta (espacio + batería).
 * Mapea la tabla "espacios_analisis_baterias".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "espacios_analisis_baterias")
@IdClass(AnalysisSpaceBatteryId.class)
public class AnalysisSpaceBatteryEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio_analisis", nullable = false)
    private AnalysisSpaceEntity analysisSpace;

    @Id
    @Column(name = "id_registro_gestion_bateria", nullable = false)
    private Long batteryManagementRecordId;

    @Column(name = "fecha_agregado", nullable = false)
    private LocalDateTime addedAt;
}
