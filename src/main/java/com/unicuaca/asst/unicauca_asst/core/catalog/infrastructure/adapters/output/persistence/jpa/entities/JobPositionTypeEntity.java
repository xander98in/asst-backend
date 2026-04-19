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
 * Entidad que representa el catálogo de tipos de cargo.
 *
 * <p>Mapea la tabla {@code tipo_cargo} en la base de datos. El ID del tipo de cargo determina
 * el formulario intralaboral aplicable en la batería: IDs 1-2 (Jefatura y Profesional) → Forma A
 * (ILA); IDs 3-4 (Auxiliar y Operario) → Forma B (ILB).</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipo_cargo")
public class JobPositionTypeEntity {

    /**
     * Identificador único del tipo de cargo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cargo")
    private Long id;

    /**
     * Nombre del tipo de cargo.
     */
    @Column(name = "nombre", nullable = false, length = 120)
    private String name;
}
