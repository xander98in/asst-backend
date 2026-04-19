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
 * Entidad que representa el catálogo de tipos de salario.
 *
 * <p>Mapea la tabla {@code tipo_salario} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipo_salario")
public class SalaryTypeEntity {

    /**
     * Identificador único del tipo de salario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_salario")
    private Long id;

    /**
     * Nombre del tipo de salario.
     */
    @Column(name = "nombre", nullable = false, length = 120)
    private String name;

}
