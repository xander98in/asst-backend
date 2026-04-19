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
 * Entidad que representa el catálogo de niveles educativos.
 *
 * <p>Mapea la tabla {@code nivel_estudio} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nivel_estudio")
public class EducationLevelEntity {

    /**
     * Identificador único del nivel educativo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel_estudio")
    private Long id;

    /**
     * Nombre del nivel educativo.
     */
    @Column(name = "nombre", nullable = false, length = 60)
    private String name;
}
