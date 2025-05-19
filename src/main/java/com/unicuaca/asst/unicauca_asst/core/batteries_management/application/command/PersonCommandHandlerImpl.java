package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonCommandCUInputPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PersonCommandHandlerImpl implements PersonCommandHandler {

    private final PersonCommandCUInputPort personCommandCUInputPort;
    private final PersonMapper personMapper;

    @Override
    public ApiResponse<PersonResponseDTO> createPerson(PersonCreateRequestDTO dto) {
        Person domain = personMapper.toDomain(dto);
        Person created = personCommandCUInputPort.createPerson(domain);
        PersonResponseDTO response = personMapper.toResponseDTO(created);
        return ApiResponse.success("Persona creada exitosamente", response);
    }

}
