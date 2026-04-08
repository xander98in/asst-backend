package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;

/**
 * Puerto de entrada para operaciones de consulta sobre el agregado {@link UserStatus}.
 *
 * Define las firmas de los métodos que deben implementar los casos de uso encargados
 * de recuperar información de estados de usuario desde el dominio.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface UserStatusQueryCUInputPort {

    /**
     * Lista todos los estados de usuario disponibles en el sistema.
     *
     * @return lista de todos los estados de usuario
     */
    List<UserStatus> getAllUserStatuses();
}
