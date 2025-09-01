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

/** Entidad que representa los tipos de identificación
 *
 * Mapeará los tipos de identificación a la base de datos.
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
    private Long idIdentificationType;

    /**
     * Descripción del tipo de identificación.
     */
    @Column(name = "descripcion", nullable = false, length = 120)
    private String description;

    /**
     * Abreviatura del tipo de identificación.
     */
    @Column(name = "abreviatura", nullable = false, length = 10)
    private String abbreviation;
}
