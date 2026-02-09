package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un registro de gestión de cuestionario.
 *
 * Cada registro de gestión de cuestionario:
 * - Está asociado a un único registro de gestión de batería.
 * - Está asociado a un único cuestionario.
 * - Tiene un estado específico (CREADO, PROCESADO, RADICADO, etc.).
 *
 * Mapea la tabla "registros_gestion_cuestionarios".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "registros_gestion_cuestionarios")
public class QuestionnaireManagementRecordEntity extends AuditableEntity {

   /**
     * Identificador único del registro de gestión de cuestionario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Registro de gestión de batería al que pertenece este registro de gestión de cuestionario.
     *
     * Relación muchos-a-uno.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_registro_gestion_bateria",
        nullable = false
    )
    private BatteryManagementRecordEntity batteryManagementRecord;

    /**
     * Cuestionario asociado a este registro de gestión de cuestionario.
     *
     * Relación muchos-a-uno:
     * - Un cuestionario puede estar asociado a varios registros de gestión de cuestionarios.
     * - Un registro de gestión de cuestionario siempre está asociado a un único cuestionario.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_cuestionario",
        nullable = false
    )
    private QuestionnaireEntity questionnaire;

    /**
     * Estado actual del registro de gestión de cuestionario.
     *
     * Relación muchos-a-uno.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_estado_registro_gestion_cuestionario",
        nullable = false
    )
    private QuestionnaireManagementRecordStatusEntity status;

    /**
     * Constructor útil para creación de nuevos registros.
     */
    public QuestionnaireManagementRecordEntity(
            BatteryManagementRecordEntity batteryManagementRecord,
            QuestionnaireEntity questionnaire,
            QuestionnaireManagementRecordStatusEntity status
    ) {
        this.batteryManagementRecord = batteryManagementRecord;
        this.questionnaire = questionnaire;
        this.status = status;
    }
}
