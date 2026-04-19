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
 * Entidad que representa el catálogo de tipos de contrato.
 *
 * <p>Mapea la tabla {@code tipo_contrato} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipo_contrato")
public class ContractTypeEntity {

    /**
     * Identificador único del tipo de contrato.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_contrato")
    private Long id;

    /**
     * Nombre del tipo de contrato.
     */
    @Column(name = "nombre", nullable = false, length = 60)
    private String name;
}
