package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.GenderEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.persistence.jpa.entities.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una persona evaluada en el sistema.
 * 
 * Mapea la tabla "personas_evaluadas" en la base de datos, que almacena información básica de identificación
 * como nombres, apellidos, tipo de identificación, sexo, año de nacimiento y correo electrónico.
 *
 * Esta entidad hereda los campos de auditoría {@code createdAt} y {@code updatedAt} desde {@link AuditableEntity}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "personas_evaluadas")
public class PersonEvaluatedEntity extends AuditableEntity {

    /**
     * Identificador único de la persona evaluada.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo de identificación asociado a la persona evaluada.
     * Relación muchos a uno.
     */
    @ManyToOne
    @JoinColumn(name = "tipo_identificacion_id", nullable = false)
    private IdentificationTypeEntity identificationType;

    /**
     * Número de identificación único de la persona evaluada.
     */
    @Column(name = "numero_identificacion", nullable = false, unique = true, length = 30)
    private String identificationNumber;

    /**
     * Nombres de pila de la persona evaluada.
     */
    @Column(name = "nombres", nullable = false, length = 120)
    private String firstName;

    /**
     * Apellidos completos de la persona evaluada.
     */
    @Column(name = "apellidos", nullable = false, length = 120)
    private String lastName;

    /**
     * Sexo asociado a la persona evaluada.
     * Relación muchos a uno.
     */
    @ManyToOne
    @JoinColumn(name = "sexo_id", nullable = false)
    private GenderEntity gender;

    /**
     * Año de nacimiento de la persona evaluada.
     */
    @Column(name = "anio_nacimiento", nullable = false)
    private Integer birthYear;

    /**
     * Correo electrónico de la persona evaluada.
     * Debe ser único y no nulo.
     */
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Constructor sin ID, útil para operaciones de creación.
     */
    public PersonEvaluatedEntity(String identificationNumber, String firstName, String lastName, Integer birthYear,
                                 String email, IdentificationTypeEntity identificationType, GenderEntity gender) {
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.email = email;
        this.identificationType = identificationType;
        this.gender = gender;
    }
}
