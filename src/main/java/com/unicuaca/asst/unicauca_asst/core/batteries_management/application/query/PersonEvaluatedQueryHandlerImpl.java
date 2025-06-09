package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonEvaluatedMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedQueryCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del manejador de consultas de persona.
 *
 * Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
public class PersonEvaluatedQueryHandlerImpl implements PersonEvaluatedQueryHandler {

    private final PersonEvaluatedQueryCUInputPort personEvaluatedQueryCUInputPort;
    private final PersonEvaluatedMapper personEvaluatedMapper;

    /**
     * Consulta una persona por su ID y devuelve una respuesta formateada.
     *
     * @param idPerson ID único de la persona
     * @return respuesta con los datos de la persona consultada
     */
    @Override
    public ApiResponse<PersonEvaluatedResponseDTO> getPersonEvaluatedById(Long idPerson) {
        PersonEvaluated person = personEvaluatedQueryCUInputPort.getPersonEvaluatedById(idPerson);
        PersonEvaluatedResponseDTO dto = personEvaluatedMapper.toResponseDTO(person);
        return ApiResponse.success("Persona consultada exitosamente", dto);
    }

}
