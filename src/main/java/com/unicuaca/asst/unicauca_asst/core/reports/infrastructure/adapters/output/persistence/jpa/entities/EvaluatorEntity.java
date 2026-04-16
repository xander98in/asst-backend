package com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.output.persistence.jpa.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
 * Entidad JPA que representa a un evaluador. Mapea la tabla "evaluadores".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "evaluadores")
public class EvaluatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluador")
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String fullName;

    @Column(name = "numero_identificacion", nullable = false, length = 30)
    private String identificationNumber;

    @Column(name = "profesion", nullable = false, length = 100)
    private String profession;

    @Column(name = "postgrado", length = 150)
    private String postgraduateDegree;

    @Column(name = "tarjeta_profesional", nullable = false, length = 30)
    private String professionalCardNumber;

    @Column(name = "licencia_salud_ocupacional", nullable = false, length = 30)
    private String occupationalHealthLicense;

    @Column(name = "fecha_expedicion_licencia", nullable = false)
    private LocalDate licenseExpirationDate;

    @Column(name = "id_usuario_creador", nullable = false)
    private Long creatorUserId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime createdAt;
}
