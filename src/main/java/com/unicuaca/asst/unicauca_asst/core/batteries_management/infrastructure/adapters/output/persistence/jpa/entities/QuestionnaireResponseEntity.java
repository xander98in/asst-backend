package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una respuesta a una pregunta de un cuestionario.
 *
 * <p>Mapea la tabla "respuestas_cuestionarios" en la base de datos.</p>
 *
 * <p>Cada respuesta está asociada a:
 * - Un registro de gestión de cuestionario (a través de {@code questionnaireManagementRecord}).
 * - Una pregunta específica (a través de {@code question}).
 * - Una opción de respuesta seleccionada (a través de {@code answerOption}).</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "respuestas_cuestionarios",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_respuesta_registro_pregunta",
            columnNames = {"id_registro_gestion_cuestionario", "id_pregunta"}
        )
    }
)
public class QuestionnaireResponseEntity extends AuditableEntity {

    /**
     * Identificador único de la respuesta al cuestionario.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Registro de gestión de cuestionario al que pertenece esta respuesta.
     *
     * <p>Relación muchos-a-uno:
     * muchas respuestas pueden estar asociadas a un mismo registro de gestión de cuestionario.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_registro_gestion_cuestionario", nullable = false)
    private QuestionnaireManagementRecordEntity questionnaireManagementRecord;

    /**
     * Pregunta a la que corresponde esta respuesta.
     *
     * <p>Relación muchos-a-uno:
     * muchas respuestas pueden estar asociadas a una misma pregunta.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private QuestionEntity question;

    /**
     * Opción de respuesta seleccionada para esta pregunta.
     *
     * <p>Relación muchos-a-uno:
     * muchas respuestas pueden estar asociadas a una misma opción de respuesta.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_opcion_respuesta", nullable = false)
    private AnswerOptionEntity answerOption;
}
