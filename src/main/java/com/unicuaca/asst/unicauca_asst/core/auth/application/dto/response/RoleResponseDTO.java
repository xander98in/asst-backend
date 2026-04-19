package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para representar los datos de un rol del sistema.
 *
 * <p>Expone el identificador, el nombre descriptivo y la clave técnica del rol,
 * utilizada por Spring Security para la autorización.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    /** Identificador único del rol. */
    @Schema(example = "1", description = "ID único del rol")
    private Long id;

    /** Nombre descriptivo del rol. */
    @Schema(example = "Administrador", description = "Nombre descriptivo del rol")
    private String name;

    /** Clave técnica del rol utilizada por Spring Security (por ejemplo, {@code ADMIN}). */
    @Schema(example = "ADMIN", description = "Clave técnica del rol")
    private String keyName;
}
