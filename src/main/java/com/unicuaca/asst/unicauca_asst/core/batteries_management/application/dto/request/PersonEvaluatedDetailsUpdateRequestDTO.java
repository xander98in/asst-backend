package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar los detalles adicionales de una persona evaluada.
 * Nota: es independiente del DTO de creación para permitir cambios futuros sin afectar create.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({PersonEvaluatedDetailsUpdateRequestDTO.class, FirstGroup.class, SecondGroup.class})
public class PersonEvaluatedDetailsUpdateRequestDTO {

    //@NotNull(message = "{personEvaluatedDetails.batteryManagementRecordId.notNull}", groups = FirstGroup.class)
    //@Min(value = 1, message = "{personEvaluatedDetails.batteryManagementRecordId.min}", groups = SecondGroup.class)
    //private Long batteryManagementRecordId;

    @NotNull(message = "{personEvaluatedDetails.genderId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.genderId.min}", groups = SecondGroup.class)
    private Long genderId;

    @NotNull(message = "{personEvaluatedDetails.civilStatusId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.civilStatusId.min}", groups = SecondGroup.class)
    private Long civilStatusId;

    @NotNull(message = "{personEvaluatedDetails.educationLevelId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.educationLevelId.min}", groups = SecondGroup.class)
    private Long educationLevelId;

    @NotBlank(message = "{personEvaluatedDetails.profession.notBlank}", groups = FirstGroup.class)
    @Size(min=1, max=250, message = "{personEvaluatedDetails.profession.size}", groups = SecondGroup.class)
    private String profession;

    @NotNull(message = "{personEvaluatedDetails.residenceCityId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.residenceCityId.min}", groups = SecondGroup.class)
    private Long residenceCityId;

    @NotNull(message = "{personEvaluatedDetails.socioeconomicLevelId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.socioeconomicLevelId.min}", groups = SecondGroup.class)
    private Long socioeconomicLevelId;

    @NotNull(message = "{personEvaluatedDetails.housingTypeId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.housingTypeId.min}", groups = SecondGroup.class)
    private Long housingTypeId;

    @NotNull(message = "{personEvaluatedDetails.dependentsCount.notNull}", groups = FirstGroup.class)
    @Min(value = 0, message = "{personEvaluatedDetails.dependentsCount.min}", groups = SecondGroup.class)
    private Integer dependentsCount;

    @NotNull(message = "{personEvaluatedDetails.workCityId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.workCityId.min}", groups = SecondGroup.class)
    private Long workCityId;

    @NotNull(message = "{personEvaluatedDetails.yearsAtCompany.notNull}", groups = FirstGroup.class)
    @Min(value = 0, message = "{personEvaluatedDetails.yearsAtCompany.min}", groups = SecondGroup.class)
    @Max(value = 99, message = "{personEvaluatedDetails.yearsAtCompany.max}", groups = SecondGroup.class)
    private Integer yearsAtCompany;

    @NotBlank(message = "{personEvaluatedDetails.jobTitle.notBlank}", groups = FirstGroup.class)
    @Size(min=1, max=150, message = "{personEvaluatedDetails.jobTitle.size}", groups = SecondGroup.class)
    private String jobTitle;

    @NotNull(message = "{personEvaluatedDetails.jobPositionTypeId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.jobPositionTypeId.min}", groups = SecondGroup.class)
    private Long jobPositionTypeId;

    @NotNull(message = "{personEvaluatedDetails.yearsInPosition.notNull}", groups = FirstGroup.class)
    @Min(value = 0, message = "{personEvaluatedDetails.yearsInPosition.min}", groups = SecondGroup.class)
    @Max(value = 99, message = "{personEvaluatedDetails.yearsInPosition.max}", groups = SecondGroup.class)
    private Integer yearsInPosition;

    @NotBlank(message = "{personEvaluatedDetails.workAreaName.notBlank}", groups = FirstGroup.class)
    @Size(min=1, max=250, message = "{personEvaluatedDetails.workAreaName.size}", groups = SecondGroup.class)
    private String workAreaName;

    @NotNull(message = "{personEvaluatedDetails.contractTypeId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.contractTypeId.min}", groups = SecondGroup.class)
    private Long contractTypeId;

    @NotNull(message = "{personEvaluatedDetails.dailyWorkHours.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.dailyWorkHours.min}", groups = SecondGroup.class)
    private Integer dailyWorkHours;

    @NotNull(message = "{personEvaluatedDetails.salaryTypeId.notNull}", groups = FirstGroup.class)
    @Min(value = 1, message = "{personEvaluatedDetails.salaryTypeId.min}", groups = SecondGroup.class)
    private Long salaryTypeId;
}
