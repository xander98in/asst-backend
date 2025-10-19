package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un cuestionario dentro del sistema.
 * Cada cuestionario puede corresponder a instrumentos como:
 * Cuestionario Intralaboral (Forma A o B), Cuestionario Extralaboral o Cuestionario de Estrés.
 *
 * Mapea la tabla "cuestionarios" en la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "cuestionarios",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_cuestionario_nombre", columnNames = {"nombre"}),
        @UniqueConstraint(name = "uk_cuestionario_abreviatura", columnNames = {"abreviatura"})
    }
)
public class QuestionnaireEntity {

    /**
     * Identificador único del cuestionario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del cuestionario (único), por ejemplo:
     * "Intralaboral - Forma A - B", "Extralaboral", "Estrés".
     */
    @Column(name = "nombre", nullable = false, length = 250)
    private String name;

    /**
     * Abreviatura del cuestionario, por ejemplo: "ILA", "ILB", "EXT", "EST".
     * */
    @Column(name = "abreviatura", nullable = false, length = 10, unique = true)
    private String abbreviation;

    /**
     * Descripción detallada u observaciones del cuestionario.
     */
    @Column(name = "descripcion", length = 1000)
    private String description;

    /**
     * Constructor útil para creación de nuevos registros.
     */
    public QuestionnaireEntity(String name, String abbreviation, String description) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
    }
}
