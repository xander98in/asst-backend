package com.unicuaca.asst.unicauca_asst.core.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de respuesta para representar los datos de un rol del sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    @Schema(example = "1", description = "ID único del rol")
    private Long id;

    @Schema(example = "Administrador", description = "Nombre descriptivo del rol")
    private String name;

    @Schema(example = "ADMIN", description = "Clave técnica del rol")
    private String keyName;
}
