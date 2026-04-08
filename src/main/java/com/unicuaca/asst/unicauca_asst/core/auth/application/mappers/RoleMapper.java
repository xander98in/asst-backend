package com.unicuaca.asst.unicauca_asst.core.auth.application.mappers;

import org.mapstruct.Mapper;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.RoleResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;

/**
 * Mapper que convierte entre el modelo de dominio {@link Role}
 * y el DTO de respuesta del módulo de autenticación.
 *
 * Utiliza MapStruct para mapear automáticamente los campos.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    /**
     * Convierte un modelo de dominio {@link Role} en un DTO de respuesta.
     *
     * @param role modelo de dominio a convertir
     * @return DTO de respuesta con los datos del rol
     */
    RoleResponseDTO toResponseDTO(Role role);
}
