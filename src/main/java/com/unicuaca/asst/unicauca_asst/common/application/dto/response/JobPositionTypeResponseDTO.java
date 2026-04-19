package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para representar un tipo de cargo.
 *
 * <p>El ID recibido determina el formulario intralaboral que debe aplicarse en la
 * batería: IDs 1-2 (Jefatura y Profesional) → Forma A (ILA); IDs 3-4 (Auxiliar y
 * Operario) → Forma B (ILB). La selección del cuestionario se resuelve en el frontend.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPositionTypeResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Jefatura - tiene personal a cargo")
    private String name;
}
