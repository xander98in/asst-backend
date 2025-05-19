package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una persona en el sistema.
 *
 * Mapea la tabla "personas" en la base de datos, que almacena información básica de identificación
 * como nombres, apellidos, tipo de identificación y sexo.
 *
 * Se utiliza en la capa de persistencia para interactuar con la base de datos mediante JPA/Hibernate.
 *
 * <p><b>Nota:</b> Los nombres de la tabla y columnas se mantienen en español por consistencia con la base de datos.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
public class PersonEntity {

    /**
     * Identificador único de la persona.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de identificación único de la persona.
     */
    @Column(name = "numero_identificacion", nullable = false, unique = true, length = 50)
    private String identificationNumber;

    /**
     * Nombres de pila de la persona.
     */
    @Column(name = "nombres", nullable = false, length = 50)
    private String firstName;

    /**
     * Apellidos completos de la persona.
     */
    @Column(name = "apellidos", nullable = false, length = 50)
    private String lastName;

    /**
     * Año de nacimiento de la persona.
     */
    @Column(name = "anio_nacimiento", nullable = false, length = 4)
    private Integer birthYear;

    /**
     * Tipo de identificación asociado a la persona.
     * Relación muchos a uno.
     */
    @ManyToOne
    @JoinColumn(name = "tipo_identificacion_id", nullable = false)
    private IdentificationTypeEntity identificationType;

    /**
     * Sexo asociado a la persona.
     * Relación muchos a uno.
     */
    @ManyToOne
    @JoinColumn(name = "sexo_id", nullable = false)
    private GenderEntity gender;

    /**
     * Constructor de conveniencia para crear una persona sin ID (útil en operaciones de creación).
     *
     * @param identificationNumber número de identificación
     * @param firstName nombres
     * @param lastName apellidos
     * @param birthYear año de nacimiento
     */
    public PersonEntity(String identificationNumber, String firstName, String lastName, Integer birthYear) {
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }
}
