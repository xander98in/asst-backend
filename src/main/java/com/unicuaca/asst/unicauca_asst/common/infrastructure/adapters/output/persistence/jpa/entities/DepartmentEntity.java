package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un departamento geográfico dentro del sistema.
 *
 * Un departamento funciona como una división territorial principal
 * dentro de la estructura geográfica. Cada departamento puede tener
 * asociadas múltiples ciudades.
 *
 * Esta entidad mapea la tabla "departamentos" en la base de datos.
 * Se valida que tanto el código como el nombre del departamento sean únicos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "departamentos",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_departamento_codigo", columnNames = {"codigo"}),
        @UniqueConstraint(name = "uk_departamento_nombre", columnNames = {"nombre"})
    }
)
@ToString
public class DepartmentEntity {

    /**
     * Identificador único del departamento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código del departamento (por ejemplo, codigo 001).
     */
    @Column(name = "codigo", nullable = false, length = 10)
    private String code;

    /**
     * Nombre del departamento.
     */
    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    /**
     * Relación bidireccional: un departamento puede tener múltiples ciudades.
     *
     * - LAZY: evita cargar las ciudades hasta que sea necesario.
     * - CascadeType.ALL: las operaciones sobre el departamento se propagan a las ciudades.
     * - orphanRemoval = true: si una ciudad se elimina del conjunto, se elimina de la base de datos.
     * - @ToString.Exclude: previene ciclos recursivos al generar el toString().
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<CityEntity> cities = new HashSet<>();

    /**
     * Método auxiliar para agregar una ciudad y mantener sincronizada la relación bidireccional.
     */
    public void addCity(CityEntity city) {
        cities.add(city);
        city.setDepartment(this);
    }

    /**
     * Método auxiliar para eliminar una ciudad y mantener sincronizada la relación bidireccional.
     */
    public void removeCity(CityEntity city) {
        cities.remove(city);
        city.setDepartment(null);
    }
}
