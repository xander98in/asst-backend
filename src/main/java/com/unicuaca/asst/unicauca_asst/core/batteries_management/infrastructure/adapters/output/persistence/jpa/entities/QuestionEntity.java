package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una pregunta asociada a un cuestionario.
 *
 * Mapea la tabla "preguntas" en la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "preguntas")
public class QuestionEntity {

    /**
     * Identificador único de la pregunta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Texto de la pregunta.
     */
    @Column(name = "texto", nullable = false, length = 1000)
    private String text;

    /**
     * Posición u orden de la pregunta dentro de un cuestionario.
     */
    @Column(name = "orden", nullable = false)
    private Integer order;

    /**
     * Cuestionario al que pertenece esta pregunta.
     *
     * Relación muchos-a-uno:
     * muchas preguntas pueden estar asociadas a un mismo cuestionario.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_cuestionario",
        nullable = false
    )
    private QuestionnaireEntity questionnaire;

    /**
     * Constructor útil para creación de nuevos registros.
     */
    public QuestionEntity(String text, Integer order, QuestionnaireEntity questionnaire) {
        this.text = text;
        this.order = order;
        this.questionnaire = questionnaire;
    }

}
