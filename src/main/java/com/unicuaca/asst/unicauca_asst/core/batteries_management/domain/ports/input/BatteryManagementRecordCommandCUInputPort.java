package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;

public interface BatteryManagementRecordCommandCUInputPort {

    BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId);
}
