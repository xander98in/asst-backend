package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

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
 * Clase entidad que mapea la tabla "sexos" en la base de datos.
 * 
 * Esta entidad representa una categoría de sexo (por ejemplo: "Masculino", "Femenino", "Otro")
 * asociada a una persona evaluada dentro del contexto del módulo de gestión de baterías.
 * 
 * Se utiliza en la capa de persistencia para interactuar con la base de datos mediante JPA/Hibernate.
 * 
 * <p><b>Nota:</b> Los nombres de la tabla y columnas se mantienen en español por consistencia con la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sexos")
public class GenderEntity {

    /**
     * Identificador único del sexo.
     * Se genera automáticamente usando la estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción textual del sexo.
     * Debe ser única y no nula.
     * 
     * Ejemplos: "Masculino", "Femenino", "Otro"
     */
    @Column(name = "descripcion", nullable = false, unique = true, length = 20)
    private String description;

    /**
     * Constructor de conveniencia para crear una instancia sin ID.
     * 
     * @param description descripción del sexo
     */
    public GenderEntity(String description) {
        this.description = description;
    }
}
