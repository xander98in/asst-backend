package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para representar los datos de un estado de usuario del sistema.
 *
 * <p>Expone el identificador y el nombre del estado (Activo, Inactivo o Bloqueado)
 * para ser utilizado en los catálogos de la API.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusResponseDTO {

    /** Identificador único del estado de usuario. */
    @Schema(example = "1", description = "ID único del estado de usuario")
    private Long id;

    /** Nombre del estado de usuario (por ejemplo, "Activo"). */
    @Schema(example = "Activo", description = "Nombre del estado de usuario")
    private String name;
}