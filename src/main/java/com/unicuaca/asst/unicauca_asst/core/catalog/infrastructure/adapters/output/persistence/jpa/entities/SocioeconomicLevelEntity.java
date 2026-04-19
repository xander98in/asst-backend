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
 * Entidad que representa el catálogo de niveles socioeconómicos.
 *
 * <p>Mapea la tabla {@code estrato} en la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estrato")
public class SocioeconomicLevelEntity {

    /**
     * Identificador único del nivel socioeconómico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estrato")
    private Long id;

    /**
     * Nombre del nivel socioeconómico.
     */
    @Column(name = "nombre", nullable = false, length = 15)
    private String name;

}
