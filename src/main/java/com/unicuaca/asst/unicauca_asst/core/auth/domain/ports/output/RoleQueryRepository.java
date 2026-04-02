package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.output;

import java.util.List;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;

/**
 * Puerto de salida para operaciones de consulta sobre el agregado {@link Role}.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura encargados
 * de recuperar información de roles desde fuentes externas.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface RoleQueryRepository {

    /**
     * Consulta un rol por su clave técnica.
     *
     * @param keyName clave técnica del rol (por ejemplo: ADMIN, PROFESIONAL_ASST)
     * @return un {@link Optional} con el rol encontrado o vacío si no existe
     */
    Optional<Role> getRoleByKeyName(String keyName);

    /**
     * Consulta un rol por su identificador único.
     *
     * @param id identificador del rol
     * @return un {@link Optional} con el rol encontrado o vacío si no existe
     */
    Optional<Role> getRoleById(Long id);

    /**
     * Lista todos los roles disponibles.
     *
     * @return lista de todos los roles
     */
    List<Role> getAllRoles();
}
