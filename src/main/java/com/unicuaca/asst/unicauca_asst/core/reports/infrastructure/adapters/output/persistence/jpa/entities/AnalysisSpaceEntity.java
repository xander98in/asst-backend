package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que representa un espacio de análisis.
 * Mapea la tabla "espacios_analisis".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "espacios_analisis")
public class AnalysisSpaceEntity {

    /** Identificador único autogenerado del espacio de análisis. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio_analisis")
    private Long id;

    /** Nombre descriptivo del espacio de análisis. */
    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    /** Evaluador responsable del espacio (relación obligatoria, carga perezosa). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evaluador", nullable = false)
    private EvaluatorEntity evaluator;

    /** ID del usuario del sistema que creó el espacio. */
    @Column(name = "id_usuario_creador", nullable = false)
    private Long creatorUserId;

    /** Fecha y hora de creación del espacio de análisis. */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime createdAt;

    /** Baterías asociadas al espacio de análisis (relación con cascade y orphanRemoval). */
    @OneToMany(mappedBy = "analysisSpace", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnalysisSpaceBatteryEntity> batteries;
}
