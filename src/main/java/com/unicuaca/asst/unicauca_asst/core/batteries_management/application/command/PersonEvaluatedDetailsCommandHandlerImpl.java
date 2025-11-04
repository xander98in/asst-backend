package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonEvaluatedDetailsMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsCommandCUInputPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del manejador de comandos para PersonEvaluatedDetails.
 *
 * Nota: Esta clase es una "impresión" sin lógica de negocio aún.
 * Solo deja trazas y marca el método como no implementado.
 */
@RequiredArgsConstructor
@Component
@Transactional
public class PersonEvaluatedDetailsCommandHandlerImpl implements PersonEvaluatedDetailsCommandHandler {

    private final PersonEvaluatedDetailsCommandCUInputPort personEvaluatedDetailsCommandCUInputPort;
    private final PersonEvaluatedDetailsMapper personEvaluatedDetailsMapper;

    @Override
    public PersonEvaluatedDetailsResponseDTO createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto) {
        System.out.println("Creando person evaluated details");
        System.out.println("Datos recibidos: " + dto);
        PersonEvaluatedDetails personEvaluatedDetails = personEvaluatedDetailsMapper.toDomain(dto);
        System.out.println("Entidad mapeada: " + personEvaluatedDetails); 

        System.out.println("---- ENTIDAD MAPEADA ----");
    System.out.println("ID: " + personEvaluatedDetails.getId());
    System.out.println("BatteryManagementRecord ID: " + 
        (personEvaluatedDetails.getBatteryManagementRecord() != null ? personEvaluatedDetails.getBatteryManagementRecord().getId() : "null"));
    System.out.println("PersonEvaluated: " + personEvaluatedDetails.getPersonEvaluated());
    System.out.println("Gender ID: " + 
        (personEvaluatedDetails.getGender() != null ? personEvaluatedDetails.getGender().getName() : "null"));
    System.out.println("CivilStatus ID: " + 
        (personEvaluatedDetails.getCivilStatus() != null ? personEvaluatedDetails.getCivilStatus().getId() : "null"));
    System.out.println("EducationLevel ID: " + 
        (personEvaluatedDetails.getEducationLevel() != null ? personEvaluatedDetails.getEducationLevel().getId() : "null"));
    System.out.println("Profession: " + personEvaluatedDetails.getProfession());
    System.out.println("ResidenceCity ID: " + 
        (personEvaluatedDetails.getResidenceCity() != null ? personEvaluatedDetails.getResidenceCity().getId() : "null"));
    System.out.println("SocioeconomicLevel ID: " + 
        (personEvaluatedDetails.getSocioeconomicLevel() != null ? personEvaluatedDetails.getSocioeconomicLevel().getId() : "null"));
    System.out.println("HousingType ID: " + 
        (personEvaluatedDetails.getHousingType() != null ? personEvaluatedDetails.getHousingType().getId() : "null"));
    System.out.println("DependentsCount: " + personEvaluatedDetails.getDependentsCount());
    System.out.println("WorkCity ID: " + 
        (personEvaluatedDetails.getWorkCity() != null ? personEvaluatedDetails.getWorkCity().getId() : "null"));
    System.out.println("YearsAtCompany: " + personEvaluatedDetails.getYearsAtCompany());
    System.out.println("JobTitle: " + personEvaluatedDetails.getJobTitle());
    System.out.println("JobPositionType ID: " + 
        (personEvaluatedDetails.getJobPositionType() != null ? personEvaluatedDetails.getJobPositionType().getId() : "null"));
    System.out.println("YearsInPosition: " + personEvaluatedDetails.getYearsInPosition());
    System.out.println("WorkAreaName: " + personEvaluatedDetails.getWorkAreaName());
    System.out.println("ContractType ID: " + 
        (personEvaluatedDetails.getContractType() != null ? personEvaluatedDetails.getContractType().getId() : "null"));
    System.out.println("DailyWorkHours: " + personEvaluatedDetails.getDailyWorkHours());
    System.out.println("SalaryType ID: " + 
        (personEvaluatedDetails.getSalaryType() != null ? personEvaluatedDetails.getSalaryType().getId() : "null"));
    System.out.println("--------------------------");

        PersonEvaluatedDetails createdDetails = personEvaluatedDetailsCommandCUInputPort.createPersonEvaluatedDetails(personEvaluatedDetails);
        return null;
    }
}
