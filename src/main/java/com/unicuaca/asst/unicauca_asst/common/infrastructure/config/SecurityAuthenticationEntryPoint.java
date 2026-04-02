package com.unicuaca.asst.unicauca_asst.common.infrastructure.config;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Punto de entrada de autenticación de Spring Security.
 *
 * <p>Se ejecuta cuando un usuario no autenticado intenta acceder a un recurso protegido.
 * Escribe directamente en el {@link HttpServletResponse} una respuesta JSON con el formato
 * estándar {@code ApiResponse<ErrorResponse<Void>>} y código HTTP 401.</p>
 */
@Component
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    /**
     * Maneja la excepción de autenticación y devuelve una respuesta 401 estructurada.
     *
     * @param request       solicitud HTTP que originó el error
     * @param response      respuesta HTTP donde se escribirá el JSON
     * @param authException excepción de autenticación lanzada por Spring Security
     * @throws IOException      si ocurre un error al escribir en la respuesta
     * @throws ServletException si ocurre un error del servlet
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        SecurityResponseWriter.writeErrorResponse(
            request,
            response,
            objectMapper,
            messageSource,
            HttpServletResponse.SC_UNAUTHORIZED,
            ErrorCode.UNAUTHORIZED,
            "user.auth.unauthorized"
        );
    }
}
