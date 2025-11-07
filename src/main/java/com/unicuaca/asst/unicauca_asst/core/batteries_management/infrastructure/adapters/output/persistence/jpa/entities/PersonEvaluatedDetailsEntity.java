package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.*;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa los detalles complementarios de una persona evaluada.
 *
 * Mapea la tabla "personas_evaluadas_detalles" y se asocia 1:1 con la persona evaluada.
 * Contiene información demográfica, residencial y laboral.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "personas_evaluadas_detalles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_personas_evaluadas_detalles_registro_gestion_bateria",
            columnNames = "id_registro_gestion_bateria"
        )
    }
)
public class PersonEvaluatedDetailsEntity {

    /**
     * Identificador único del detalle de persona evaluada.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Registro de gestión de batería asociado (relación 1:1).
     * Se garantiza unicidad por constraint de tabla.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_registro_gestion_bateria", nullable = false, unique = true)
    private BatteryManagementRecordEntity batteryManagementRecord;

    /**
     * Estado civil de la persona evaluada (ManyToOne).
     * Ej.: soltero, casado, divorciado, etc.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_estado_civil", nullable = true)
    private CivilStatusEntity civilStatus;

    /**
     * Nivel de estudios de la persona evaluada (ManyToOne).
     * Ej.: bachiller, técnico, profesional, maestría, doctorado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nivel_estudio", nullable = true)
    private EducationLevelEntity educationLevel;

    /**
     * Profesión (cadena hasta 250 caracteres). Puede ser nula.
     */
    @Column(name = "profesion", length = 250)
    private String profession;

    /**
     * Ciudad de residencia (ManyToOne). Opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_ciudad_residencia", nullable = true)
    private CityEntity residenceCity;

    /**
     * Estrato socioeconómico (ManyToOne). Opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_estrato", nullable = true)
    private SocioeconomicLevelEntity socioeconomicLevel;

    /**
     * Tipo de vivienda (ManyToOne). Opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_tipo_vivienda", nullable = true)
    private HousingTypeEntity housingType;

    /**
     * Número de personas que dependen económicamente de la persona evaluada. Puede ser nulo.
     */
    @Column(name = "numero_dependientes")
    private Integer dependentsCount;

    /**
     * Ciudad donde trabaja actualmente (ManyToOne). Opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_ciudad_trabajo", nullable = true)
    private CityEntity workCity;

    /**
     * Años trabajados en la empresa actual (cadena hasta 12). Puede ser nulo.
     * Ejemplos de formato aceptado: "3", "3.5", "10+" (defínelo a nivel de dominio/validación).
     */
    @Column(name = "anios_en_empresa", length = 12)
    private String yearsAtCompany;

    /**
     * Cargo actual por nombre (cadena hasta 150). Puede ser nulo.
     */
    @Column(name = "cargo", length = 150)
    private String jobTitle;

    /**
     * Tipo de cargo (ManyToOne). Opcional. Ej.: jefatura, auxiliar, operario.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_tipo_cargo", nullable = true)
    private JobPositionTypeEntity jobPositionType;

    /**
     * Años desempeñando el cargo (cadena hasta 12). Puede ser nulo.
     */
    @Column(name = "anios_en_cargo", length = 12)
    private String yearsInPosition;

    /**
     * Nombre del departamento o área en la que trabaja (cadena hasta 250).
     */
    @Column(name = "area_trabajo", length = 250)
    private String workAreaName;

    /**
     * Tipo de contrato (ManyToOne). Opcional. Ej.: término indefinido, temporal, prestación de servicios.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_tipo_contrato", nullable = true)
    private ContractTypeEntity contractType;

    /**
     * Número de horas diarias de trabajo (entero). Puede ser nulo.
     */
    @Column(name = "horas_diarias_trabajo")
    private Integer dailyWorkHours;

    /**
     * Tipo de salario (ManyToOne). Opcional. Ej.: fijo, variable, por comisión.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_tipo_salario", nullable = true)
    private SalaryTypeEntity salaryType;
}
