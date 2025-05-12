package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import org.springframework.http.HttpStatus;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonQueryCUInputPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonQueryHandlerImpl implements PersonQueryHandler {

    private final PersonQueryCUInputPort personQueryCUInputPort;
    private final PersonMapper personMapper;

    @Override
    public ApiResponse<PersonResponseDTO> getPersonById(Long idPerson) {
        Person person = personQueryCUInputPort.getById(idPerson);
        PersonResponseDTO dto = personMapper.toResponseDTO(person);

        return new ApiResponse<>(
            HttpStatus.OK,
            "Persona consultada exitosamente",
            dto,
            1000
        );
    }

}
