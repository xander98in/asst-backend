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
 * Entidad que representa el catálogo de estados civiles.
 *
 * <p>Mapea la tabla {@code estado_civil} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estado_civil")
public class CivilStatusEntity {

    /**
     * Identificador único del estado civil.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_civil")
    private Long id;

    /**
     * Nombre del estado civil.
     */
    @Column(name = "nombre", nullable = false, length = 60)
    private String name;
}
