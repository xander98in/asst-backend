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
 * Entidad que representa el catálogo de tipos de identificación.
 *
 * <p>Mapea la tabla {@code tipos_identificacion} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipos_identificacion")
public class IdentificationTypeEntity {

    /**
     * Identificador único del tipo de identificación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_identificacion")
    private Long id;

    /**
     * Nombre del tipo de identificación.
     */
    @Column(name = "nombre", nullable = false, length = 120)
    private String name;

    /**
     * Abreviatura del tipo de identificación.
     */
    @Column(name = "abreviatura", nullable = false, length = 10)
    private String abbreviation;
}
