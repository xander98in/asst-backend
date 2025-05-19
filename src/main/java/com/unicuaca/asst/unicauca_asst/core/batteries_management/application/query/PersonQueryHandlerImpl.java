package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonQueryCUInputPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PersonQueryHandlerImpl implements PersonQueryHandler {

    private final PersonQueryCUInputPort personQueryCUInputPort;
    private final PersonMapper personMapper;

    @Override
    public ApiResponse<PersonResponseDTO> getPersonById(Long idPerson) {
        Person person = personQueryCUInputPort.getPersonById(idPerson);
        PersonResponseDTO dto = personMapper.toResponseDTO(person);
        return ApiResponse.success("Persona consultada exitosamente", dto);
    }

}
