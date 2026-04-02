package com.unicuaca.asst.unicauca_asst.common.infrastructure.config;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Manejador de acceso denegado de Spring Security.
 *
 * <p>Se ejecuta cuando un usuario autenticado intenta acceder a un recurso
 * para el cual no tiene los permisos necesarios. Escribe directamente en el
 * {@link HttpServletResponse} una respuesta JSON con el formato estándar
 * {@code ApiResponse<ErrorResponse<Void>>} y código HTTP 403.</p>
 */
@Component
@RequiredArgsConstructor
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Maneja la excepción de acceso denegado y devuelve una respuesta 403 estructurada.
     *
     * @param request               solicitud HTTP que originó el error
     * @param response              respuesta HTTP donde se escribirá el JSON
     * @param accessDeniedException excepción de acceso denegado lanzada por Spring Security
     * @throws IOException      si ocurre un error al escribir en la respuesta
     * @throws ServletException si ocurre un error del servlet
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        SecurityResponseWriter.writeErrorResponse(
            request,
            response,
            objectMapper,
            messageSource,
            HttpServletResponse.SC_FORBIDDEN,
            ErrorCode.FORBIDDEN,
            "user.auth.forbidden"
        );
    }
}
