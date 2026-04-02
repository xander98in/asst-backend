package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request;

import com.unicuaca.asst.unicauca_asst.common.validation.FirstGroup;
import com.unicuaca.asst.unicauca_asst.common.validation.SecondGroup;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la actualización del estado de un usuario del sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GroupSequence({FirstGroup.class, SecondGroup.class, SystemUserStatusUpdateRequestDTO.class})
public class SystemUserStatusUpdateRequestDTO {

    /**
     * Nombre del nuevo estado a asignar (ej. "Activo", "Inactivo", "Bloqueado").
     */
    @NotBlank(message = "{systemUser.statusName.notBlank}", groups = FirstGroup.class)
    private String statusName;
}
