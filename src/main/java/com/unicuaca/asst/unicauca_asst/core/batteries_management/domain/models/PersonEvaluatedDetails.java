package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models;

import com.unicuaca.asst.unicauca_asst.common.domain.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Representa los detalles adicionales de una persona evaluada,
 * incluyendo información demográfica, laboral y socioeconómica.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class PersonEvaluatedDetails {

    private Long id;

    private BatteryManagementRecord batteryManagementRecord;

    private Gender gender;

    private CivilStatus civilStatus;

    private EducationLevel educationLevel;

    private String profession;

    private City residenceCity;

    private SocioeconomicLevel socioeconomicLevel;

    private HousingType housingType;

    private Integer dependentsCount;

    private City workCity;

    private String yearsAtCompany;

    private String jobTitle;

    private JobPositionType jobPositionType;

    private String yearsInPosition;

    private String workAreaName;

    private ContractType contractType;

    private Integer dailyWorkHours;

    private SalaryType salaryType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
