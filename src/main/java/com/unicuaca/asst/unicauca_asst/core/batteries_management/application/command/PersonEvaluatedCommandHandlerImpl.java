package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonEvaluatedMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedCommandCUInputPort;

import lombok.RequiredArgsConstructor;

/**
 * Maneja el comando de creación de una persona evaluada.
 * Convierte el DTO de entrada a modelo de dominio, ejecuta el caso de uso y retorna el DTO de salida.
 * 
 * <p>Su responsabilidad principal es convertir los datos de entrada (DTO) en modelos de dominio,
 * delegar la ejecución del caso de uso al puerto correspondiente y transformar la respuesta del dominio
 * en un DTO de salida adecuado para el cliente.</p>
 * 
 */
@RequiredArgsConstructor
@Component
@Transactional
public class PersonEvaluatedCommandHandlerImpl implements PersonEvaluatedCommandHandler {

    private final PersonEvaluatedCommandCUInputPort personCommandCUInputPort;
    private final PersonEvaluatedMapper personEvaluatedMapper;

    /**
     * Crea una persona evaluada y devuelve su representación.
     *
     * @param dto datos de entrada
     * @return respuesta con el DTO de la persona creada
     */
    @Override
    public PersonEvaluatedResponseDTO createPersonEvaluated(PersonEvaluatedCreateRequestDTO dto) {
        PersonEvaluated domain = personEvaluatedMapper.toDomain(dto);
        PersonEvaluated created = personCommandCUInputPort.createPersonEvaluated(domain);
        return personEvaluatedMapper.toResponseDTO(created);
    }

    /**
     * Orquesta el proceso de actualización desde el handler.
     *
     * @param id identificador de la persona a actualizar
     * @param dto datos nuevos validados
     * @return respuesta estandarizada con DTO actualizado
     */
    @Override
    public PersonEvaluatedResponseDTO updatePersonEvaluated(Long id, PersonEvaluatedUpdateRequestDTO dto) {
        PersonEvaluated domain = personEvaluatedMapper.toDomain(id, dto);
        PersonEvaluated updated = personCommandCUInputPort.updatePersonEvaluated(domain);
        return personEvaluatedMapper.toResponseDTO(updated);
    }
}
