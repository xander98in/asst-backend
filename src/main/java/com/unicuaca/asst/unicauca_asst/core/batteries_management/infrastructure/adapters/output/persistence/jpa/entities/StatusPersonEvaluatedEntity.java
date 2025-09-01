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
 * Entidad que representa los estados de una persona evaluada.
 * Mapeo de la tabla "estados_persona_evaluada"
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estados_persona_evaluada")
public class StatusPersonEvaluatedEntity {

    /**
     * Identificador único del estado de la persona evaluada.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_persona_evaluada")
    private Long id;

    /**
     * Nombre del estado de la persona evaluada.
     */
    @Column(name = "nombre", nullable = false, length = 30)
    private String name;
}
