package com.unicuaca.asst.unicauca_asst.common.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

/**
 * Clase genérica para encapsular respuestas estándar de la API REST.
 * Puede representar tanto respuestas exitosas como errores estructurados.
 *
 * @param <T> tipo de dato que contiene el campo 'data' (puede ser DTO o ErrorResponse)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ApiResponse")
public class ApiResponse<T> {

    /**
     * Código funcional estandarizado de la operación (p. ej. {@code ASST-0001}, {@code ASST-0002}).
     * No es el código HTTP. Suele mapear a {@link SuccessCode}.
     */
    @Schema(example = "ASST-0002", description = "Código funcional estandarizado de la operación.")
    private String code;

    /**
     * Mensaje humano que describe el resultado (éxito o error) de la operación.
     */
    @Schema(example = "Mensaje del resultado", description = "Mensaje descriptivo del resultado.")
    private String message;

    /**
     * Datos de la respuesta. En éxito, contiene el DTO o colección solicitada.
     * En error, puede transportar detalles (p. ej., un objeto ErrorResponse).
     */
    @Schema(description = "Datos retornados por la operación. En error puede incluir detalles.", type = "object")
    private T data;

    /**
     * Ruta del endpoint que generó la respuesta. Útil para auditoría y trazabilidad.
     */
    @Schema(example = "/asst/battery-management-record/create/123", description = "Ruta del endpoint que atendió la solicitud.")
    private String path;

    /**
     * Marca de tiempo generada por el servidor en el momento de construir la respuesta,
     * en formato ISO-8601 con offset.
     */
    @Schema(example = "2025-10-04T17:45:12.123-05:00", description = "Fecha/hora del servidor en que se construyó la respuesta.")
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();

    /**
     * Fábrica conveniente para construir una respuesta completa en una sola línea.
     *
     * @param code    Código funcional (p. ej., {@code ASST-0002}).
     * @param message Mensaje humano de resultado.
     * @param data    Cuerpo de la respuesta.
     * @param path    Ruta del endpoint invocado.
     */
    public static <T> ApiResponse<T> of(String code, String message, T data, String path) {
        return ApiResponse.<T>builder()
            .code(code)
            .message(message)
            .data(data)
            .path(path)
            .timestamp(OffsetDateTime.now())
            .build();
    }
}
