package com.unicuaca.asst.unicauca_asst.common.domain.models;

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
public class Department {

    private Long id;
    private String code;
    private String name;

    /**
     * Conjunto de ciudades del departamento.
     * Nota: Mantener con cuidado para evitar ciclos al mapear/serializar.
     */
    @Builder.Default
    private Set<City> cities = new HashSet<>();

    // Helpers opcionales para mantener consistencia en el dominio (si los usas)
    public void addCity(City city) {
        this.cities.add(city);
        city.setDepartment(this);
    }

    public void removeCity(City city) {
        this.cities.remove(city);
        city.setDepartment(null);
    }
}
