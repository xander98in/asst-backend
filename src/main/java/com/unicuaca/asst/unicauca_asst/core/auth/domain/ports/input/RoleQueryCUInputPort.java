package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.Role;

/**
 * Puerto de entrada para operaciones de consulta sobre el agregado {@link Role}.
 *
 * Define las firmas de los métodos que deben implementar los casos de uso encargados
 * de recuperar información de roles desde el dominio.
 *
 * <p>Hace parte de la arquitectura hexagonal, separando las dependencias externas
 * de la lógica del dominio.</p>
 */
public interface RoleQueryCUInputPort {

    /**
     * Lista todos los roles disponibles en el sistema.
     *
     * @return lista de todos los roles
     */
    List<Role> getAllRoles();

    /**
     * Consulta un rol por su clave técnica.
     *
     * @param keyName clave técnica del rol (por ejemplo: ADMIN, PROFESIONAL_ASST)
     * @return una instancia del modelo de dominio {@link Role}
     */
    Role getRoleByKeyName(String keyName);
}
