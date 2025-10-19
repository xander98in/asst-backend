package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.mappers.BatteryManagementRecordMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordCommandCUInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class BatteryManagementRecordCommandHandlerImpl implements BatteryManagementRecordCommandHandler {

    private final BatteryManagementRecordCommandCUInputPort batteryManagementRecordCommandCUInputPort;
    private final BatteryManagementRecordMapper batteryManagementRecordMapper;

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @return El DTO del registro de gestión de baterías creado.
     */
    @Override
    public BatteryManagementRecordResponseDTO createBatteryManagementRecord(Long personEvaluatedId) {
        BatteryManagementRecord record = batteryManagementRecordCommandCUInputPort.createBatteryManagementRecord(personEvaluatedId);
        return batteryManagementRecordMapper.toResponseDTO(record);
    }
}
