package com.unicuaca.asst.unicauca_asst.common.exceptions.structure;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Clase que representa una respuesta estandarizada de error utilizada en el manejo global
 * de excepciones dentro de la aplicación. Esta estructura puede ser retornada al cliente
 * cuando ocurre un error, proporcionando información clara, trazable y consistente.
 *
 * @param <T> Tipo de datos adicionales, útil por ejemplo en errores de validación por campo.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {

    /**
     * Código de error definido por la aplicación (ej: ASST-BUS-0001).
     */
    @Schema(example = "ASST-BUS-0001", description = "Código de error definido por la aplicación")
    private String errorCode;

    /**
     * Mensaje descriptivo del error ocurrido.
     */
    @Schema(example = "Recurso no encontrado", description = "Mensaje descriptivo del error ocurrido")
    private String message;

    /**
     * Método HTTP de la petición que generó el error.
     */
    @Accessors(chain = true)
    @Schema(example = "POST", description = "Método HTTP de la petición que generó el error")
    private String method;

    /**
     * Información adicional del error. Solo se incluirá si no es null.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Información adicional del error, si aplica")
    private T data;
}
