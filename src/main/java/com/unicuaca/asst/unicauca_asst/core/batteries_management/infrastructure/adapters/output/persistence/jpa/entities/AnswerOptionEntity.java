package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una opción de respuesta posible en los cuestionarios.
 * Ejemplo: "Siempre" (Valor 5), "Nunca" (Valor 1), etc.
 *
 * Mapea la tabla "opciones_respuesta" en la base de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "opciones_respuesta")
public class AnswerOptionEntity {

    /**
     * Identificador único de la opción de respuesta.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Texto descriptivo de la opción de respuesta.
     * Ejemplo: "Siempre", "Casi siempre", "A veces", "Casi nunca", "Nunca".
     */
    @Column(name = "texto", nullable = false, length = 100)
    private String text;

    /**
     * Valor numérico asociado a la opción de respuesta.
     * Ejemplo: "Siempre" = 5, "Casi siempre" = 4, "A veces" = 3, "Casi nunca" = 2, "Nunca" = 1.
     */
    @Column(name = "valor", nullable = false, unique = true)
    private Integer value;

    /**
     * Posición u orden de la opción de respuesta dentro de un conjunto de opciones.
     * Esto es útil para mantener un orden específico al mostrar las opciones en la interfaz de usuario.
     */
    @Column(name = "orden", nullable = false)
    private Integer order;
}
