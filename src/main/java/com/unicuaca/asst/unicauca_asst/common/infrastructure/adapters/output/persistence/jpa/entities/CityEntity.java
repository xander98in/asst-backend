package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una ciudad dentro del sistema.
 *
 * Cada ciudad pertenece a un departamento y se encuentra identificada
 * por un código único (por ejemplo, código 001) y un nombre.
 *
 * Esta entidad mapea la tabla "ciudades" en la base de datos, donde
 * se valida que tanto el código como el nombre sean únicos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "ciudades"
)
@ToString
public class CityEntity {

    /**
     * Identificador único de la ciudad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código de la ciudad (por ejemplo, código DANE).
     */
    @Column(name = "codigo", nullable = false, length = 10)
    private String code;

    /**
     * Nombre de la ciudad.
     */
    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    /**
     * Relación Many-To-One: muchas ciudades pertenecen a un departamento.
     *
     * - LAZY: la carga del departamento se realiza bajo demanda.
     * - optional = false: toda ciudad debe estar asociada a un departamento.
     * - La columna "departamento_id" contiene la FK con un constraint nombrado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "departamento_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ciudad_departamento")
    )
    @ToString.Exclude
    private DepartmentEntity department;
}
