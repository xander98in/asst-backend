package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.models.*;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsCommandCUInputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonEvaluatedDetailsCommandService implements PersonEvaluatedDetailsCommandCUInputPort {

    private final CatalogQueryRepository catalogQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    @Override
    public PersonEvaluatedDetails createPersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails) {

        System.out.println("Sexo: " + personEvaluatedDetails.getGender());
        Long genderId = personEvaluatedDetails.getGender().getId();
        System.out.println("Id: " + genderId);

        Gender gender = this.catalogQueryRepository.getGenderById(genderId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El género con ID " + genderId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
        System.out.println("gender: " + gender);
        personEvaluatedDetails.setGender(gender);

        System.out.println("createPersonEvaluatedDetails");
        System.out.println("datos de estado civil: " + personEvaluatedDetails.getCivilStatus());
        Long civilStatusId = personEvaluatedDetails.getCivilStatus().getId();

        CivilStatus civilStatus = this.catalogQueryRepository.getCivilStatusById(civilStatusId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado civil con ID " + civilStatusId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });

        System.out.println("civilStatus: " + civilStatus);
        personEvaluatedDetails.setCivilStatus(civilStatus);

        Long educationLevelId = personEvaluatedDetails.getEducationLevel().getId();
        EducationLevel educationLevel = this.catalogQueryRepository.getEducationLevelById(educationLevelId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El nivel de educación con ID " + educationLevelId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });

        personEvaluatedDetails.setEducationLevel(educationLevel);
        System.out.println("educationLevel: " + educationLevel);

        System.out.println("profession: " + personEvaluatedDetails.getProfession());
        personEvaluatedDetails.setProfession(normalizeText(personEvaluatedDetails.getProfession()));
        System.out.println("profession normalizada: " + personEvaluatedDetails.getProfession());

        System.out.println("datos de ciudad de residencia: " + personEvaluatedDetails.getResidenceCity());
        Long residenceCityId = personEvaluatedDetails.getResidenceCity().getId();
        System.out.println("residenceCityId: " + residenceCityId);
        City residenceCity = this.catalogQueryRepository.getCityById(residenceCityId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La ciudad de residencia con ID " + residenceCityId + " no fue encontrada.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
        System.out.println("residenceCity: " + residenceCity);
        personEvaluatedDetails.setResidenceCity(residenceCity);

        Long socioeconomicLevelId = personEvaluatedDetails.getSocioeconomicLevel().getId();
        System.out.println("socioeconomicLevel: " + personEvaluatedDetails.getSocioeconomicLevel());
        SocioeconomicLevel socioeconomicLevel = this.catalogQueryRepository.getSocioeconomicLevelById(socioeconomicLevelId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El nivel socioeconómico con ID " + socioeconomicLevelId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });

        personEvaluatedDetails.setSocioeconomicLevel(socioeconomicLevel);
        System.out.println("socioeconomicLevel: " + socioeconomicLevel);

        System.out.println("datos de la vivienda: " + personEvaluatedDetails.getHousingType());
        Long housingTypeId = personEvaluatedDetails.getHousingType().getId();
        System.out.println("housingTypeId: " + housingTypeId);

        HousingType housingType = this.catalogQueryRepository.getHousingTypeById(housingTypeId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El tipo de vivienda con ID " + housingTypeId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });

        personEvaluatedDetails.setHousingType(housingType);
        System.out.println("housingType: " + housingType);

        System.out.println("Numero de dependientes: " + personEvaluatedDetails.getDependentsCount());

        System.out.println("Ciudad de trabajo: " + personEvaluatedDetails.getWorkCity());
        Long workCityId = personEvaluatedDetails.getWorkCity().getId();
        System.out.println("workCityId: " + workCityId);
        City workCity = this.catalogQueryRepository.getCityById(workCityId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La ciudad de trabajo con ID " + workCityId + " no fue encontrada.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
        personEvaluatedDetails.setWorkCity(workCity);
        System.out.println("workCity: " + workCity);

        System.out.println("Years At Company: " + personEvaluatedDetails.getYearsAtCompany());
        personEvaluatedDetails.setYearsAtCompany(normalizeText(personEvaluatedDetails.getYearsAtCompany()));
        System.out.println("Years At Company normalizada: " + personEvaluatedDetails.getYearsAtCompany());

        System.out.println("Job Title: " + personEvaluatedDetails.getJobTitle());
        personEvaluatedDetails.setJobTitle(normalizeText(personEvaluatedDetails.getJobTitle()));
        System.out.println("Job Title normalizada: " + personEvaluatedDetails.getJobTitle());

        System.out.println("datos de tipo de cargo: " + personEvaluatedDetails.getJobPositionType());
        Long jobPositionTypeId = personEvaluatedDetails.getJobPositionType().getId();
        System.out.println("jobPositionTypeId: " + jobPositionTypeId);
        JobPositionType jobPositionType = this.catalogQueryRepository.getJobPositionTypeById(jobPositionTypeId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El tipo de cargo con ID " + jobPositionTypeId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
        personEvaluatedDetails.setJobPositionType(jobPositionType);
        System.out.println("jobPositionType: " + jobPositionType);

        System.out.println("Years In Position: " + personEvaluatedDetails.getYearsInPosition());
        personEvaluatedDetails.setYearsInPosition(normalizeText(personEvaluatedDetails.getYearsInPosition()));
        System.out.println("Years In Position normalizada: " + personEvaluatedDetails.getYearsInPosition());

        System.out.println("Work Area Name: " + personEvaluatedDetails.getWorkAreaName());
        personEvaluatedDetails.setWorkAreaName(normalizeText(personEvaluatedDetails.getWorkAreaName()));
        System.out.println("Work Area Name normalizada: " + personEvaluatedDetails.getWorkAreaName());

        System.out.println("datos de tipo de contrato: " + personEvaluatedDetails.getContractType());
        Long contractTypeId = personEvaluatedDetails.getContractType().getId();
        System.out.println("contractTypeId: " + contractTypeId);
        ContractType contractType = this.catalogQueryRepository.getContractTypeById(contractTypeId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El tipo de contrato con ID " + contractTypeId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
        personEvaluatedDetails.setContractType(contractType);
        System.out.println("contractType: " + contractType);

        System.out.println("Daily Work Hours: " + personEvaluatedDetails.getDailyWorkHours());

        System.out.println("datos de tipo de salario: " + personEvaluatedDetails.getSalaryType());
        Long salaryTypeId = personEvaluatedDetails.getSalaryType().getId();
        System.out.println("salaryTypeId: " + salaryTypeId);
        SalaryType salaryType = this.catalogQueryRepository.getSalaryTypeById(salaryTypeId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El tipo de salario con ID " + salaryTypeId + " no fue encontrado.")
                );
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
        personEvaluatedDetails.setSalaryType(salaryType);
        System.out.println("salaryType: " + salaryType);



        return null;
    }

    /**
     * Normaliza un texto: elimina espacios y convierte la primera letra en mayúscula.
     */
    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        value = value.trim();
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
