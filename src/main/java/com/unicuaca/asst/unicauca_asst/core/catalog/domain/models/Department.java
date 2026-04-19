package com.unicuaca.asst.unicauca_asst.core.catalog.domain.models;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Representa un departamento geográfico en el dominio.
 * Puede contener un conjunto de ciudades (opcional según el caso de uso).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Department {

    private Long id;
    private String code;
    private String name;

    /**
     * Conjunto de ciudades del departamento.
     * Nota: Mantener con cuidado para evitar ciclos al mapear/serializar.
     */
    @Builder.Default
    @ToString.Exclude
    private Set<City> cities = new HashSet<>();

    /**
     * Agrega una ciudad al departamento y mantiene la relación bidireccional
     * asignando este departamento como su contenedor.
     *
     * @param city ciudad a agregar
     */
    public void addCity(City city) {
        this.cities.add(city);
        city.setDepartment(this);
    }

    /**
     * Remueve una ciudad del departamento y desvincula la referencia inversa
     * para evitar un estado inconsistente en la ciudad.
     *
     * @param city ciudad a remover
     */
    public void removeCity(City city) {
        this.cities.remove(city);
        city.setDepartment(null);
    }
}
