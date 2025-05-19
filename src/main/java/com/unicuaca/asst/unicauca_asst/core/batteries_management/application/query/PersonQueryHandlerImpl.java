package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de persona.
 *
 * Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
public class PersonQueryHandlerImpl implements PersonQueryHandler {

    private final PersonQueryCUInputPort personQueryCUInputPort;
    private final PersonMapper personMapper;

    /**
     * Consulta una persona por su ID y devuelve una respuesta formateada.
     *
     * @param idPerson ID único de la persona
     * @return respuesta con los datos de la persona consultada
     */
    @Override
    public ApiResponse<PersonResponseDTO> getPersonById(Long idPerson) {
        Person person = personQueryCUInputPort.getPersonById(idPerson);
        PersonResponseDTO dto = personMapper.toResponseDTO(person);
        return ApiResponse.success("Persona consultada exitosamente", dto);
    }

}
