package com.unicuaca.asst.unicauca_asst.core.auth.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.auth.domain.models.SystemUser;

/**
 * Puerto de entrada para el caso de uso de autenticación.
 *
 * Define la operación de inicio de sesión mediante token de Google OAuth,
 * permitiendo que los adaptadores de entrada interactúen con la lógica
 * de autenticación sin acoplarse directamente a su implementación.
 *
 * <p>Forma parte de la capa de dominio dentro de la arquitectura hexagonal.</p>
 */
public interface AuthCUInputPort {

    /**
     * Autentica un usuario del sistema mediante su correo electrónico previamente verificado.
     *
     * La verificación del token de Google es responsabilidad de la infraestructura
     * (GoogleTokenService). Este método recibe el email ya verificado y valida
     * la existencia y estado del usuario en el sistema.
     *
     * @param email correo electrónico del usuario ya verificado por Google OAuth
     * @return el usuario autenticado como instancia de {@link SystemUser}
     */
    SystemUser loginWithGoogle(String email);
}
