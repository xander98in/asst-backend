package com.unicuaca.asst.unicauca_asst.core.auth.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.request.SystemUserUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.SystemUserResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;

/**
 * Mapper que convierte entre el modelo de dominio {@link SystemUser}
 * y los DTOs de solicitud/respuesta del módulo de autenticación.
 *
 * Utiliza MapStruct para mapear automáticamente los campos,
 * incluyendo la conversión de objetos anidados (status, roles).
 */
@Mapper(componentModel = "spring")
public interface SystemUserMapper {

    /**
     * Convierte un DTO de creación en un modelo de dominio {@link SystemUser}.
     *
     * Los roles se resuelven por IDs en el servicio de dominio,
     * por lo que se inicializan como un HashSet vacío.
     *
     * @param dto datos del usuario a crear
     * @return modelo de dominio con los datos básicos
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "personEvaluatedId", source = "personEvaluatedId")
    @Mapping(target = "roles", expression = "java(dto.getRoleIds() != null ? dto.getRoleIds().stream().map(id -> new com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role(id, null, null)).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SystemUser toDomain(SystemUserCreateRequestDTO dto);

    /**
     * Convierte un DTO de actualización en un modelo de dominio con ID.
     *
     * @param id  identificador del usuario a actualizar
     * @param dto datos actualizados del usuario
     * @return modelo de dominio con ID y datos modificables
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "fullName", source = "dto.fullName")
    @Mapping(target = "personEvaluatedId", source = "dto.personEvaluatedId")
    @Mapping(target = "roles", expression = "java(dto.getRoleIds() != null ? dto.getRoleIds().stream().map(roleId -> new com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role(roleId, null, null)).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SystemUser toDomain(Long id, SystemUserUpdateRequestDTO dto);

    /**
     * Convierte un modelo de dominio {@link SystemUser} en un DTO de respuesta.
     *
     * Mapea el estado como su nombre y los roles como sus claves técnicas (keyName).
     *
     * @param systemUser modelo de dominio a convertir
     * @return DTO de respuesta con los datos públicos del usuario
     */
    @Mapping(target = "status", expression = "java(systemUser.getStatus() != null ? systemUser.getStatus().getName() : null)")
    @Mapping(target = "roles", expression = "java(systemUser.getRoles() != null ? systemUser.getRoles().stream().map(r -> r.getKeyName()).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())")
    SystemUserResponseDTO toResponseDTO(SystemUser systemUser);
}
