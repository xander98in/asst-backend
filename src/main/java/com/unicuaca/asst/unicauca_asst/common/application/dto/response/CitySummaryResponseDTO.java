package com.unicuaca.asst.unicauca_asst.common.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitySummaryResponseDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "001")
    private String code;

    @Schema(example = "Popayán")
    private String name;
}
