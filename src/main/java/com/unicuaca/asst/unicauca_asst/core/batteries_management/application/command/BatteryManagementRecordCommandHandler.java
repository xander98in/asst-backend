package com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.BatteryManagementRecordResponseDTO;

public interface BatteryManagementRecordCommandHandler {

    BatteryManagementRecordResponseDTO createBatteryManagementRecord(Long personEvaluatedId);
}
