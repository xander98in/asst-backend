package com.unicuaca.asst.unicauca_asst.core.auth.application.command;

import com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response.AuthResponseDTO;

/**
 * Manejador de comandos para operaciones de autenticación.
 *
 * <p>Define el contrato para procesar el inicio de sesión de un usuario
 * cuyo correo ha sido previamente verificado por el proveedor externo (Google OAuth).</p>
 */
public interface AuthCommandHandler {

    /**
     * Procesa el inicio de sesión de un usuario ya verificado por Google OAuth.
     *
     * @param email correo electrónico del usuario ya verificado
     * @return respuesta con los datos de autenticación y el usuario
     */
    AuthResponseDTO login(String email);
}
