package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un cuestionario dentro del sistema.
 * Cada cuestionario puede corresponder a instrumentos como:
 * Cuestionario Intralaboral (Forma A o B), Cuestionario Extralaboral o Cuestionario de Estrés.
 *
 * <p>Mapea la tabla "cuestionarios" en la base de datos.</p>
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
     * Lista de preguntas asociadas a este cuestionario.
     *
     * <p>Relación uno-a-muchos:
     * un cuestionario puede tener muchas preguntas asociadas.</p>
     */
    @Builder.Default
    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions = new ArrayList<>();

    /**
     * Constructor útil para creación de nuevos registros de cuestionario antes de persistirlos.
     *
     * @param name nombre único del cuestionario
     * @param abbreviation abreviatura única del cuestionario (ej.: ILA, ILB, EXT, EST)
     * @param description descripción detallada u observaciones del cuestionario
     */
    public QuestionnaireEntity(String name, String abbreviation, String description) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.description = description;
    }
}
