package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedDetailsMetaResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.PersonEvaluatedDetailsMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsQueryCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del manejador de consultas para detalles de personas evaluadas.
 *
 * Delega la lógica de negocio al puerto de entrada y transforma
 * el modelo de dominio en un DTO para la respuesta.
 */
@RequiredArgsConstructor
@Component
@Transactional
public class PersonEvaluatedDetailsQueryHandlerImpl implements PersonEvaluatedDetailsQueryHandler{

    private final PersonEvaluatedDetailsQueryCUInputPort personEvaluatedDetailsQueryCUInputPort;
    private final PersonEvaluatedDetailsMapper personEvaluatedDetailsMapper;

    /**
     * Obtiene metadata del detalle de una persona evaluada asociado a un registro de gestión de bateria.
     *
     * @param batteryManagementRecordId ID del registro de gestión de batería
     * @return
     */
    @Override
    public PersonEvaluatedDetailsMetaResponseDTO getMetaByBatteryManagementRecordId(Long batteryManagementRecordId) {
        PersonEvaluatedDetails details = personEvaluatedDetailsQueryCUInputPort.getMetaByBatteryManagementRecordId(batteryManagementRecordId);
        return personEvaluatedDetailsMapper.toMetaResponseDTO(details);
    }
}
