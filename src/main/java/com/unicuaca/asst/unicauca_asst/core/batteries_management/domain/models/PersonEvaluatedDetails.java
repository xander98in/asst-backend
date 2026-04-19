package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Representa los detalles adicionales de una persona evaluada.
 *
 * <p>Consolida información demográfica (sexo, estado civil, nivel educativo),
 * socioeconómica (estrato, tipo de vivienda, personas a cargo) y laboral
 * (ciudad de trabajo, cargo, antigüedad, tipo de contrato, jornada y tipo de salario).</p>
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class PersonEvaluatedDetails {

    /** Identificador único del detalle de la persona evaluada. */
    private Long id;

    /** Registro de gestión de batería asociado a estos detalles. */
    private BatteryManagementRecord batteryManagementRecord;

    /** Sexo de la persona evaluada. */
    private Gender gender;

    /** Estado civil de la persona evaluada. */
    private CivilStatus civilStatus;

    /** Último nivel educativo alcanzado por la persona evaluada. */
    private EducationLevel educationLevel;

    /** Profesión u oficio de la persona evaluada. */
    private String profession;

    /** Ciudad de residencia de la persona evaluada. */
    private City residenceCity;

    /** Nivel socioeconómico (estrato) de la persona evaluada. */
    private SocioeconomicLevel socioeconomicLevel;

    /** Tipo de vivienda en la que habita la persona evaluada. */
    private HousingType housingType;

    /** Cantidad de personas a cargo económicamente de la persona evaluada. */
    private Integer dependentsCount;

    /** Ciudad donde la persona evaluada desempeña su trabajo. */
    private City workCity;

    /** Tiempo de antigüedad en la empresa. */
    private String yearsAtCompany;

    /** Nombre del cargo que ocupa la persona evaluada. */
    private String jobTitle;

    /** Tipo de cargo (por ejemplo, Jefatura, Profesional, Auxiliar, Operario). */
    private JobPositionType jobPositionType;

    /** Tiempo de antigüedad en el cargo actual. */
    private String yearsInPosition;

    /** Nombre del área de trabajo o dependencia. */
    private String workAreaName;

    /** Tipo de contrato laboral de la persona evaluada. */
    private ContractType contractType;

    /** Número de horas de trabajo diarias. */
    private Integer dailyWorkHours;

    /** Tipo de salario percibido por la persona evaluada. */
    private SalaryType salaryType;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    private LocalDateTime updatedAt;
}
