package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.UserStatus;

/**
 * Puerto de salida para operaciones de consulta sobre el agregado {@link UserStatus}.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura encargados
 * de recuperar información de estados de usuario desde fuentes externas.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface UserStatusQueryRepository {

    /**
     * Consulta un estado de usuario por su nombre.
     *
     * @param name nombre del estado a buscar
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    Optional<UserStatus> getUserStatusByName(String name);

    /**
     * Consulta un estado de usuario por su identificador único.
     *
     * @param id identificador del estado
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    Optional<UserStatus> getUserStatusById(Long id);

    /**
     * Lista todos los estados de usuario disponibles.
     *
     * @return lista de todos los estados de usuario
     */
    List<UserStatus> getAllUserStatuses();
}
