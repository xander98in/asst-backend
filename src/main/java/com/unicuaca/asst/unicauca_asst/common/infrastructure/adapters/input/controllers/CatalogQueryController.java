package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.CivilStatusResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.ContractTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.EducationLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.GenderResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.HousingTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.JobPositionTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SalaryTypeResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.dto.response.SocioeconomicLevelResponseDTO;
import com.unicuaca.asst.unicauca_asst.common.application.query.CatalogQueryHandler;
import com.unicuaca.asst.unicauca_asst.common.docs.IdentificationTypesApiResponseDoc;
import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST que expone los endpoints de consulta relacionados con catálogos comunes.
 *
 * Esta clase pertenece a la capa de infraestructura (adaptador de entrada) de la arquitectura hexagonal,
 * y se encarga de recibir las solicitudes HTTP desde el exterior y delegarlas al handler correspondiente
 * definido en la capa de aplicación.
 *
 * <p>Utiliza {@link CatalogQueryHandler} para ejecutar la lógica de consulta relacionada con catálogos.</p>
 */
@Tag(name = "catálogos", description = "Operaciones de consulta de catálogos comunes")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asst/catalog")
@RequiredArgsConstructor
public class CatalogQueryController {

    private final CatalogQueryHandler catalogQueryHandler;

    /**
     * Endpoint para consultar los tipos de identificación disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link IdentificationTypeResponseDTO} representando los tipos de identificación.
     */
    @Operation(
        summary = "Listar tipos de identificación",
        description = "Retorna el catálogo de tipos de identificación disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = IdentificationTypesApiResponseDoc.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Catálogo vacío o error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/id-types")
    public ResponseEntity<ApiResponse<List<IdentificationTypeResponseDTO>>> getIdTypes(HttpServletRequest request) {
        List<IdentificationTypeResponseDTO> response = catalogQueryHandler.getIdTypes();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Tipos de identificación obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los estados civiles disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link CivilStatusResponseDTO} representando los estados civiles.
     */
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/civil-statuses")
    public ResponseEntity<ApiResponse<List<CivilStatusResponseDTO>>> getCivilStatuses(HttpServletRequest request) {
        List<CivilStatusResponseDTO> response = catalogQueryHandler.getCivilStatuses();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Estados civiles obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los niveles educativos disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link EducationLevelResponseDTO} representando los niveles educativos.
     */
    @Operation(
        summary = "Listar niveles educativos",
        description = "Retorna el catálogo de niveles educativos disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/education-levels")
    public ResponseEntity<ApiResponse<List<EducationLevelResponseDTO>>> getEducationLevels(HttpServletRequest request) {
        List<EducationLevelResponseDTO> response = catalogQueryHandler.getEducationLevels();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Niveles educativos obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los tipos de vivienda disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link HousingTypeResponseDTO} representando los tipos de vivienda.
     */
    @Operation(
        summary = "Listar tipos de vivienda",
        description = "Retorna el catálogo de tipos de vivienda disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/housing-types")
    public ResponseEntity<ApiResponse<List<HousingTypeResponseDTO>>> getHousingTypes(HttpServletRequest request) {
        List<HousingTypeResponseDTO> response = catalogQueryHandler.getHousingTypes();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Tipos de vivienda obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los niveles socioeconómicos disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link SocioeconomicLevelResponseDTO} representando los niveles socioeconómicos.
     */
    @Operation(
        summary = "Listar niveles socioeconómicos",
        description = "Retorna el catálogo de niveles socioeconómicos disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/socioeconomic-levels")
    public ResponseEntity<ApiResponse<List<SocioeconomicLevelResponseDTO>>> getSocioeconomicLevels(HttpServletRequest request) {
        List<SocioeconomicLevelResponseDTO> response = catalogQueryHandler.getSocioeconomicLevels();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Niveles socioeconómicos obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los tipos de cargo disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link JobPositionTypeResponseDTO} representando los tipos de cargo.
     */
    @Operation(
        summary = "Listar tipos de cargo",
        description = "Retorna el catálogo de tipos de cargo disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/job-position-types")
    public ResponseEntity<ApiResponse<List<JobPositionTypeResponseDTO>>> getJobPositionTypes(HttpServletRequest request) {
        List<JobPositionTypeResponseDTO> response = catalogQueryHandler.getJobPositionTypes();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Tipos de cargo obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los tipos de contrato disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link ContractTypeResponseDTO} representando los tipos de contrato.
     */
    @Operation(
        summary = "Listar tipos de contrato",
        description = "Retorna el catálogo de tipos de contrato disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/contract-types")
    public ResponseEntity<ApiResponse<List<ContractTypeResponseDTO>>> getContractTypes(HttpServletRequest request) {
        List<ContractTypeResponseDTO> response = catalogQueryHandler.getContractTypes();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Tipos de contrato obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los tipos de salario disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link SalaryTypeResponseDTO} representando los tipos de salario.
     */
    @Operation(
        summary = "Listar tipos de salario",
        description = "Retorna el catálogo de tipos de salario disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/salary-types")
    public ResponseEntity<ApiResponse<List<SalaryTypeResponseDTO>>> getSalaryTypes(HttpServletRequest request) {
        List<SalaryTypeResponseDTO> response = catalogQueryHandler.getSalaryTypes();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Tipos de salario obtenidos exitosamente", response);
    }

    /**
     * Endpoint para consultar los géneros disponibles.
     *
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene una lista de
     *         {@link GenderResponseDTO} representando los géneros.
     */
    @Operation(
        summary = "Listar géneros",
        description = "Retorna el catálogo de géneros disponibles"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno",
            content = @Content(
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    @GetMapping("/genders")
    public ResponseEntity<ApiResponse<List<GenderResponseDTO>>> getGenders(HttpServletRequest request) {
        List<GenderResponseDTO> response = catalogQueryHandler.getGenders();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Géneros obtenidos exitosamente", response);
    }

}
