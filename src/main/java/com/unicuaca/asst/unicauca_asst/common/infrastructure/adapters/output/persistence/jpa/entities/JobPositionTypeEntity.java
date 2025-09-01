package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities;

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
 * Entidad que representa un tipo de cargo en el sistema.
 * Mapea la tabla "tipo_cargo" en la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipo_cargo")
public class JobPositionTypeEntity {

    /**
     * Identificador único del tipo de cargo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cargo")
    private Long id;

    /**
     * Nombre del tipo de cargo.
     */
    @Column(name = "nombre", nullable = false, length = 120)
    private String name;
}
