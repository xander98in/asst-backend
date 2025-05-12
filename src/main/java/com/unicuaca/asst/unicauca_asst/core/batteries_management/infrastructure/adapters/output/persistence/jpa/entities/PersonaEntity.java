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

@Entity
@Table(name = "personas")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_identificacion", nullable = false, unique = true, length = 50)
    private String numeroIdentificacion;

    @Column(nullable = false, length = 50)
    private String nombres;

    @Column(nullable = false, length = 50)
    private String apellidos;

    @Column(name = "anio_nacimiento", length = 4)
    private Integer anioNacimiento;

    @ManyToOne
    @JoinColumn(name = "tipo_identificacion_id", nullable = false)
    private TipoIdentificacionEntity tipoIdentificacion;

    @ManyToOne
    @JoinColumn(name = "sexo_id", nullable = false)
    private SexoEntity sexo;

    public PersonaEntity(String numeroIdentificacion, String nombres, String apellidos, Integer anioNacimiento) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.anioNacimiento = anioNacimiento;
    }
}

