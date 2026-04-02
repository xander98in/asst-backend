package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output;

import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;

/**
 * Puerto de salida que define las operaciones de comando (creación, actualización y eliminación)
 * relacionadas con el agregado {@link SystemUser}.
 *
 * Esta interfaz representa el contrato que debe implementar la infraestructura
 * para permitir la persistencia de usuarios del sistema en una fuente externa.
 *
 * <p>Hace parte del patrón de arquitectura hexagonal, permitiendo que el dominio
 * se mantenga desacoplado de los detalles técnicos de almacenamiento.</p>
 */
public interface SystemUserCommandRepository {

    /**
     * Persiste un nuevo usuario del sistema.
     *
     * @param systemUser objeto de dominio que representa al usuario a guardar
     * @return el usuario persistido con sus datos actualizados (por ejemplo, con ID generado)
     */
    Optional<SystemUser> saveSystemUser(SystemUser systemUser);

    /**
     * Actualiza un usuario del sistema existente.
     *
     * @param systemUser objeto con los datos a persistir
     * @return objeto actualizado o vacío si no se encontró
     */
    Optional<SystemUser> updateSystemUser(SystemUser systemUser);

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id identificador del usuario a eliminar
     */
    void deleteSystemUserById(Long id);
}
