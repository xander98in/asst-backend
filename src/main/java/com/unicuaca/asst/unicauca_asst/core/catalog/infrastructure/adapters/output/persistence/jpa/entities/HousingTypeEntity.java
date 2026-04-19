package com.unicuaca.asst.unicauca_asst.core.catalog.infrastructure.adapters.output.persistence.jpa.entities;

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
 * Entidad que representa el catálogo de tipos de vivienda.
 *
 * <p>Mapea la tabla {@code tipo_vivienda} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipo_vivienda")
public class HousingTypeEntity {

    /**
     * Identificador único del tipo de vivienda.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_vivienda")
    private Long id;

    /**
     * Nombre del tipo de vivienda.
     */
    @Column(name = "nombre", nullable = false, length = 30)
    private String name;
}
