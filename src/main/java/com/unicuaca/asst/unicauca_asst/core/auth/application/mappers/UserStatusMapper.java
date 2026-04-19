package com.unicuaca.asst.unicauca_asst.core.auth.application.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.UserStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;

/**
 * Mapper que convierte entre el modelo de dominio {@link UserStatus}
 * y el DTO de respuesta del módulo de autenticación.
 *
 * <p>Utiliza MapStruct para mapear automáticamente los campos.</p>
 */
@Mapper(componentModel = "spring")
public interface UserStatusMapper {

    /**
     * Convierte un modelo de dominio {@link UserStatus} en un DTO de respuesta.
     *
     * @param userStatus modelo de dominio a convertir
     * @return DTO de respuesta con los datos del estado de usuario
     */
    UserStatusResponseDTO toResponseDTO(UserStatus userStatus);
}
