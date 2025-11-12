package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa los estados de un registro de gestión de cuestionarios.
 * Mapea la tabla "estados_registro_gestion_cuestionarios".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estados_registro_gestion_cuestionarios")
public class QuestionnaireManagementRecordStatusEntity {

    /**
     * Identificador único del estado del registro de gestión de cuestionarios.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nombre del estado del registro de gestión de cuestionarios.
     */
    @Column(name = "nombre", nullable = false, length = 30, unique = true)
    private String name;

}
