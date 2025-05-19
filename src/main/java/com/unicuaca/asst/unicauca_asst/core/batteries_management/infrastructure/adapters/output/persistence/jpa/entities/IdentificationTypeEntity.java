package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un tipo de identificación en el sistema.
 *
 * Mapea la tabla "tipos_identificacion" en la base de datos.
 * Se utiliza para categorizar documentos como cédula, pasaporte, etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipos_identificacion")
public class IdentificationTypeEntity {

    /**
     * Identificador único del tipo de identificación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción del tipo de identificación (ej: "Cédula", "Pasaporte").
     */
    @Column(name = "descripcion", nullable = false, unique = true, length = 50)
    private String description;

    /**
     * Constructor de conveniencia sin ID.
     *
     * @param description descripción del tipo de identificación
     */
    public IdentificationTypeEntity(String description) {
        this.description = description;
    }
}
