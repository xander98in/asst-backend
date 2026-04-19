package com.unicuaca.asst.unicauca_asst.core.reports.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta principal que agrupa todos los resultados del informe individual
 * de calificación de riesgo psicosocial.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualReportResponseDTO {

    /** Resultado del cuestionario intralaboral con dominios y dimensiones. */
    @Schema(description = "Resultado del cuestionario intralaboral")
    private IntralaboralResultResponseDTO intralaboral;

    /** Resultado del cuestionario extralaboral con dimensiones. */
    @Schema(description = "Resultado del cuestionario extralaboral")
    private ExtralaboralResultResponseDTO extralaboral;

    /** Resultado del puntaje total general (intralaboral + extralaboral). */
    @Schema(description = "Resultado del puntaje total general (intralaboral + extralaboral)")
    private GeneralTotalResultResponseDTO generalTotal;

    /** Resultado del cuestionario de estrés. */
    @Schema(description = "Resultado del cuestionario de estrés")
    private StressResultResponseDTO stress;
}
