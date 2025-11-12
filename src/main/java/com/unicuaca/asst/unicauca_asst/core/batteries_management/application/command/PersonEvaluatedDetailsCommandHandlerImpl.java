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
 * Implementación del manejador de comandos para la creación de
 * detalles de persona evaluada (PersonEvaluatedDetails).
 *
 * <p>Su responsabilidad principal es convertir los datos de entrada (DTO) en modelos de dominio,
 * delegar la ejecución del caso de uso al puerto correspondiente y transformar la respuesta del dominio
 * en un DTO de salida adecuado para el cliente.</p>
 */
@RequiredArgsConstructor
@Component
@Transactional
public class PersonEvaluatedDetailsCommandHandlerImpl implements PersonEvaluatedDetailsCommandHandler {

    private final PersonEvaluatedDetailsCommandCUInputPort personEvaluatedDetailsCommandCUInputPort;
    private final PersonEvaluatedDetailsMapper personEvaluatedDetailsMapper;

    /** 
     * Registra los detalles de una persona evaluada en el sistema.
     *
     * @param dto datos de entrada validados para la creación
     * @return DTO de respuesta con la información creada y enriquecida desde el dominio
     */
    @Override
    public PersonEvaluatedDetailsResponseDTO createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto) {
        PersonEvaluatedDetails personEvaluatedDetails = personEvaluatedDetailsMapper.toDomain(dto);
        PersonEvaluatedDetails createdDetails = personEvaluatedDetailsCommandCUInputPort.createPersonEvaluatedDetails(personEvaluatedDetails);
        System.out.println("\n\ncreatePersonEvaluatedDetails method called with DTO: " + createdDetails);
        return null;
    }
}
