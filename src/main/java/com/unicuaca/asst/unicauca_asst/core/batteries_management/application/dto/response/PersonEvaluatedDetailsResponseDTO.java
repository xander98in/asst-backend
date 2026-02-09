package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar los detalles adicionales de una persona evaluada.
 *
 * Incluye información demográfica y laboral relevante para la evaluación de riesgo psicosocial.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEvaluatedDetailsResponseDTO {

    @Schema(example = "1", description = "ID único de los detalles de la persona evaluada")
    private Long id;

    @Schema(example = "1", description = "ID del género que hace parte de los detalles de la persona evaluada")
    private Long genderId;

    @Schema(example = "Masculino", description = "Nombre del género que hace parte de los detalles de la persona evaluada")
    private String genderName;

    @Schema(example = "1", description = "ID del estado civil que hace parte de los detalles de la persona evaluada")
    private Long civilStatusId;

    @Schema(example = "Soltero", description = "Nombre del estado civil que hace parte de los detalles de la persona evaluada")
    private String civilStatusName;

    @Schema(example = "2", description = "ID del nivel educativo que hace parte de los detalles de la persona evaluada")
    private Long educationLevelId;

    @Schema(example = "Profesional Universitario", description = "Nombre del nivel educativo que hace parte de los detalles de la persona evaluada")
    private String educationLevelName;

    @Schema(example = "Ingeniero de Sistemas", description = "Profesión que hace parte de los detalles de la persona evaluada")
    private String profession;

    @Schema(example = "1", description = "ID de la ciudad de residencia que hace parte de los detalles de la persona evaluada")
    private Long residenceCityId;

    @Schema(example = "001", description = "Código de la ciudad de residencia que hace parte de los detalles de la persona evaluada")
    private String residenceCityCode;

    @Schema(example = "Cali", description = "Nombre de la ciudad de residencia que hace parte de los detalles de la persona evaluada")
    private String residenceCityName;

    @Schema(example = "76", description = "ID del departamento de residencia que hace parte de los detalles de la persona evaluada")
    private Long residenceDepartmentId;

    @Schema(example = "76", description = "Código del departamento de residencia que hace parte de los detalles de la persona evaluada")
    private String residenceDepartmentCode;

    @Schema(example = "Valle del Cauca", description = "Nombre del departamento de residencia que hace parte de los detalles de la persona evaluada")
    private String residenceDepartmentName;

    @Schema(example = "3", description = "ID del nivel socioeconómico que hace parte de los detalles de la persona evaluada")
    private Long socioeconomicLevelId;

    @Schema(example = "Estrato 3", description = "Nombre del nivel socioeconómico que hace parte de los detalles de la persona evaluada")
    private String socioeconomicLevelName;

    @Schema(example = "2", description = "ID del tipo de vivienda que hace parte de los detalles de la persona evaluada")
    private Long housingTypeId;

    @Schema(example = "Apartamento", description = "Nombre del tipo de vivienda que hace parte de los detalles de la persona evaluada")
    private String housingTypeName;

    @Schema(example = "2", description = "Número de dependientes que hace parte de los detalles de la persona evaluada")
    private Integer dependentsCount;

    @Schema(example = "1", description = "ID de la ciudad de trabajo que hace parte de los detalles de la persona evaluada")
    private Long workCityId;

    @Schema(example = "001", description = "Código de la ciudad de trabajo que hace parte de los detalles de la persona evaluada")
    private String workCityCode;

    @Schema(example = "Cali", description = "Nombre de la ciudad de trabajo que hace parte de los detalles de la persona evaluada")
    private String workCityName;

    @Schema(example = "76", description = "ID del departamento de trabajo que hace parte de los detalles de la persona evaluada")
    private Long workDepartmentId;

    @Schema(example = "76", description = "Código del departamento de trabajo que hace parte de los detalles de la persona evaluada")
    private String workDepartmentCode;

    @Schema(example = "Valle del Cauca", description = "Nombre del departamento de trabajo que hace parte de los detalles de la persona evaluada")
    private String workDepartmentName;

    @Schema(example = "5", description = "Años en la empresa que hace parte de los detalles de la persona evaluada")
    private String yearsAtCompany;

    @Schema(example = "Analista de Sistemas", description = "Cargo que hace parte de los detalles de la persona evaluada")
    private String jobTitle;

    @Schema(example = "1", description = "ID del tipo de cargo que hace parte de los detalles de la persona evaluada")
    private Long jobPositionId;

    @Schema(example = "Operativo", description = "Nombre del tipo de cargo que hace parte de los detalles de la persona evaluada")
    private String jobPositionName;

    @Schema(example = "3", description = "Años en el cargo que hace parte de los detalles de la persona evaluada")
    private String yearsInPosition;

    @Schema(example = "Desarrollo de Software", description = "Nombre del área o departamento de trabajo que hace parte de los detalles de la persona evaluada")
    private String workAreaName;

    @Schema(example = "1", description = "ID del tipo de contrato que hace parte de los detalles de la persona evaluada")
    private Long contractTypeId;

    @Schema(example = "Indefinido", description = "Nombre del tipo de contrato que hace parte de los detalles de la persona evaluada")
    private String contractTypeName;

    @Schema(example = "8", description = "Número de horas laborales diarias que hace parte de los detalles de la persona evaluada")
    private Integer dailyWorkHours;

    @Schema(example = "2", description = "ID del tipo de salario que hace parte de los detalles de la persona evaluada")
    private Long salaryTypeId;

    @Schema(example = "Mensual", description = "Nombre del tipo de salario que hace parte de los detalles de la persona evaluada")
    private String salaryTypeName;
}
