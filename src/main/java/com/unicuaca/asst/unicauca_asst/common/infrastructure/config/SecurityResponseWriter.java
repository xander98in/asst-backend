package com.unicuaca.asst.unicauca_asst.common.infrastructure.config;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorUtils;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Utilidad para escribir respuestas de error de seguridad directamente en el {@link HttpServletResponse}.
 *
 * <p>Se utiliza desde los puntos de entrada de seguridad ({@link SecurityAuthenticationEntryPoint})
 * y manejadores de acceso denegado ({@link SecurityAccessDeniedHandler}) donde no se dispone
 * del ciclo de vida de Spring MVC para construir {@code ResponseEntity}.</p>
 *
 * <p>La respuesta generada respeta el formato estándar {@code ApiResponse<ErrorResponse<Void>>}
 * del proyecto, garantizando consistencia con el resto de errores de la aplicación.</p>
 */
public final class SecurityResponseWriter {

    /** Clase de utilidades; evita instanciación. */
    private SecurityResponseWriter() {}

    /**
     * Escribe una respuesta JSON de error con el formato estándar {@code ApiResponse<ErrorResponse<Void>>}.
     *
     * @param request        solicitud HTTP actual (para obtener path y method)
     * @param response       respuesta HTTP donde se escribirá el JSON
     * @param objectMapper   mapper para serialización JSON
     * @param messageSource  fuente de mensajes para resolución i18n
     * @param status         código HTTP de la respuesta (401, 403, etc.)
     * @param errorCode      código de error específico del catálogo {@link ErrorCode}
     * @param userMessageKey clave i18n del mensaje amigable para el usuario
     * @throws IOException si ocurre un error al escribir en la respuesta
     */
    public static void writeErrorResponse(
        HttpServletRequest request,
        HttpServletResponse response,
        ObjectMapper objectMapper,
        MessageSource messageSource,
        int status,
        ErrorCode errorCode,
        String userMessageKey
    ) throws IOException {

        String techMessage = resolveMessage(messageSource, errorCode.getMessageKey(), null, request);
        String userMessage = resolveMessage(messageSource, userMessageKey, null, request);
        String wrapperMessage = resolveMessage(
            messageSource,
            ErrorCode.AUTHENTICATION_ERROR.getMessageKey(),
            new Object[]{techMessage},
            request
        );

        var details = ErrorUtils.of(
            errorCode.getCode(),
            techMessage,
            userMessage,
            request.getMethod()
        );

        var apiResponse = ApiResponse.of(
            ErrorCode.AUTHENTICATION_ERROR.getCode(),
            wrapperMessage,
            details,
            request.getRequestURI()
        );

        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }

    /**
     * Sobrecarga que recibe el código de error como String en lugar de {@link ErrorCode}.
     *
     * <p>Se utiliza desde filtros de seguridad donde el código de error proviene
     * de una excepción capturada (como {@link com.unicuaca.asst.unicauca_asst.common.exceptions.InvalidJwtException})
     * y no se dispone del enum directamente.</p>
     *
     * @param request         solicitud HTTP actual (para obtener path y method)
     * @param response        respuesta HTTP donde se escribirá el JSON
     * @param objectMapper    mapper para serialización JSON
     * @param messageSource   fuente de mensajes para resolución i18n
     * @param status          código HTTP de la respuesta (401, 403, etc.)
     * @param errorCodeValue  código de error como String (ej: "ASST-SEC-0007")
     * @param techMessageKey  clave i18n del mensaje técnico
     * @param userMessageKey  clave i18n del mensaje amigable para el usuario
     * @throws IOException si ocurre un error al escribir en la respuesta
     */
    public static void writeErrorResponse(
        HttpServletRequest request,
        HttpServletResponse response,
        ObjectMapper objectMapper,
        MessageSource messageSource,
        int status,
        String errorCodeValue,
        String techMessageKey,
        String userMessageKey
    ) throws IOException {

        String techMessage = resolveMessage(messageSource, techMessageKey, null, request);
        String userMessage = resolveMessage(messageSource, userMessageKey, null, request);
        String wrapperMessage = resolveMessage(
            messageSource,
            ErrorCode.AUTHENTICATION_ERROR.getMessageKey(),
            new Object[]{techMessage},
            request
        );

        var details = ErrorUtils.of(
            errorCodeValue,
            techMessage,
            userMessage,
            request.getMethod()
        );

        var apiResponse = ApiResponse.of(
            ErrorCode.AUTHENTICATION_ERROR.getCode(),
            wrapperMessage,
            details,
            request.getRequestURI()
        );

        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }

    /**
     * Resuelve un mensaje i18n a partir de una clave y argumentos opcionales.
     * Si la clave no existe en el {@link MessageSource}, retorna la clave misma como fallback.
     *
     * @param messageSource fuente de mensajes
     * @param key           clave de mensaje o texto directo
     * @param args          argumentos opcionales para formatear el mensaje
     * @param request       solicitud HTTP para obtener el locale
     * @return el mensaje resuelto o la clave original si no se encuentra
     */
    private static String resolveMessage(MessageSource messageSource, String key, Object[] args, HttpServletRequest request) {
        try {
            return messageSource.getMessage(key, args, request.getLocale());
        } catch (NoSuchMessageException e) {
            return key;
        }
    }
}
