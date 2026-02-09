package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedDetailsUpdateRequestDTO;
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
     */
    @Override
    public void createPersonEvaluatedDetails(PersonEvaluatedDetailsCreateRequestDTO dto) {
        PersonEvaluatedDetails personEvaluatedDetails = personEvaluatedDetailsMapper.toDomain(dto);
        personEvaluatedDetailsCommandCUInputPort.createPersonEvaluatedDetails(personEvaluatedDetails);
    }

    /**
     * Actualiza los detalles de una persona evaluada en el sistema.
     *
     * @param personEvaluatedDetailsId ID del registro a actualizar
     * @param dto datos de entrada validados para la actualizacióN
     */
    @Override
    public void updatePersonEvaluatedDetails(Long personEvaluatedDetailsId, PersonEvaluatedDetailsUpdateRequestDTO dto) {
        PersonEvaluatedDetails personEvaluatedDetails = personEvaluatedDetailsMapper.toDomain(dto);
        personEvaluatedDetailsCommandCUInputPort.updatePersonEvaluatedDetails(personEvaluatedDetailsId, personEvaluatedDetails);
    }

    /**
     * Elimina los detalles de una persona evaluada del sistema.
     *
     * @param personEvaluatedDetailsId ID del registro a eliminar
     */
    @Override
    public void deletePersonEvaluatedDetails(Long personEvaluatedDetailsId) {
        personEvaluatedDetailsCommandCUInputPort.deletePersonEvaluatedDetails(personEvaluatedDetailsId);
    }
}
