package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;

/**
 * Puerto de entrada para los casos de uso relacionados con la creación,
 * actualización y eliminación de usuarios del sistema.
 *
 * Esta interfaz define las operaciones de tipo "Command" (crear, modificar o eliminar)
 * del modelo {@link SystemUser}, permitiendo que los adaptadores de entrada interactúen
 * con la lógica de negocio sin acoplarse directamente a su implementación.
 *
 * <p>Forma parte de la capa de dominio dentro de la arquitectura hexagonal.</p>
 */
public interface SystemUserCommandCUInputPort {

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * La implementación concreta debe encargarse de validar la información,
     * aplicar reglas de negocio y delegar la persistencia en los puertos de salida.
     *
     * @param systemUser objeto {@link SystemUser} con los datos del usuario a crear
     * @return el usuario creado, incluyendo su ID generado y datos resultantes del proceso
     */
    SystemUser createSystemUser(SystemUser systemUser);

    /**
     * Actualiza los datos de un usuario del sistema existente.
     *
     * La implementación concreta debe encargarse de validar la información,
     * aplicar reglas de negocio y delegar la persistencia en los puertos de salida.
     *
     * @param systemUser objeto de dominio con los datos modificados
     * @return el usuario actualizado
     */
    SystemUser updateSystemUser(SystemUser systemUser);

    /**
     * Cambia el estado de un usuario del sistema (activar, desactivar, bloquear).
     *
     * @param id identificador del usuario al que se le cambiará el estado
     * @param statusName nombre del nuevo estado a asignar
     * @return el usuario con el estado actualizado
     */
    SystemUser changeUserStatus(Long id, String statusName);

    /**
     * Elimina un usuario del sistema por su identificador.
     *
     * @param id identificador del usuario a eliminar
     */
    void deleteSystemUser(Long id);
}
