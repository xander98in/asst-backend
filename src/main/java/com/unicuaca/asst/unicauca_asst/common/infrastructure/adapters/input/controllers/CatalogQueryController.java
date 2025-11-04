package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers;

import java.util.List;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.*;
import com.unicuaca.asst.unicauca_asst.common.docs.ErrorResponseApiResponse;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers.docs.CityApiResponse;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers.docs.DepartmentApiResponse;
import com.unicuaca.asst.unicauca_asst.common.response.ResponseUtil;
import com.unicuaca.asst.unicauca_asst.common.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Obtiene una ciudad por código, incluyendo su departamento (sin ciudades).
     *
     * @param code código de la ciudad
     * @param request el request HTTP
     * @return ResponseEntity con la respuesta API que contiene la ciudad
     */
    @Operation(
        summary = "Obtener ciudad por código (con departamento)",
        description = "Retorna la ciudad correspondiente al código, incluyendo el departamento al que pertenece."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Ciudad encontrada",
            content = @Content(schema = @Schema(implementation = CityApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Ciudad no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/cities/by-code/{code}")
    public ResponseEntity<ApiResponse<CityResponseDTO>> getCityByCodeWithDepartment(@PathVariable String code, HttpServletRequest request) {
        CityResponseDTO dto = catalogQueryHandler.getCityByCodeWithDepartment(code);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Ciudad obtenida exitosamente", dto);
    }

    /**
     * Obtiene una ciudad por nombre, incluyendo su departamento (sin ciudades).
     *
     * @param name nombre de la ciudad
     * @param request el request HTTP
     * @return ResponseEntity con la respuesta API que contiene la ciudad
     */
    @Operation(
        summary = "Obtener ciudad por nombre (con departamento)",
        description = "Retorna la ciudad correspondiente al nombre, incluyendo el departamento al que pertenece."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Ciudad encontrada",
            content = @Content(schema = @Schema(implementation = CityApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Ciudad no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/cities/by-name/{name}")
    public ResponseEntity<ApiResponse<CityResponseDTO>> getCityByNameWithDepartment(@PathVariable String name, HttpServletRequest request) {
        CityResponseDTO dto = catalogQueryHandler.getCityByNameWithDepartment(name);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Ciudad obtenida exitosamente", dto);
    }

    /**
     * Obtiene un departamento por código, incluyendo sus ciudades
     * (cada ciudad sin el campo department).
     *
     * @param code código del departamento
     * @param request el request HTTP
     * @return ResponseEntity con la respuesta API que contiene el departamento
     */
    @Operation(
        summary = "Obtener departamento por código (con ciudades)",
        description = "Retorna el departamento correspondiente al código, incluyendo el listado de sus ciudades."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Departamento encontrado",
            content = @Content(schema = @Schema(implementation = DepartmentApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Departamento no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/departments/by-code/{code}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentByCodeWithCities(@PathVariable String code, HttpServletRequest request) {
        DepartmentResponseDTO dto = catalogQueryHandler.getDepartmentByCodeWithCities(code);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Departamento obtenido exitosamente", dto);
    }

    /**
     * Obtiene un departamento por nombre, incluyendo sus ciudades
     * (cada ciudad sin el campo department).
     *
     * @param name nombre del departamento
     * @param request el request HTTP
     * @return ResponseEntity con la respuesta API que contiene el departamento
     */
    @Operation(
        summary = "Obtener departamento por nombre (con ciudades)",
        description = "Retorna el departamento correspondiente al nombre, incluyendo el listado de sus ciudades."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Departamento encontrado",
            content = @Content(schema = @Schema(implementation = DepartmentApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Departamento no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/departments/by-name/{name}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentByNameWithCities(@PathVariable String name, HttpServletRequest request) {
        DepartmentResponseDTO dto = catalogQueryHandler.getDepartmentByNameWithCities(name);
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Departamento obtenido exitosamente", dto);
    }

    /**
     * Lista todos los departamentos (sin incluir sus ciudades).
     */
    @Operation(
        summary = "Listar departamentos (sin ciudades)",
        description = "Retorna todos los departamentos sin el listado de sus ciudades."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Catálogo vacío o error interno",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments(HttpServletRequest request) {
        List<DepartmentResponseDTO> response = catalogQueryHandler.getAllDepartments();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Departamentos obtenidos exitosamente", response);
    }

    /**
     * Lista todos los departamentos incluyendo sus ciudades
     * (cada ciudad sin el campo department).
     */
    @Operation(
        summary = "Listar departamentos con sus ciudades",
        description = "Retorna todos los departamentos con el listado de sus ciudades (cada ciudad sin su department)."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Catálogo vacío o error interno",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/departments/with-cities")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartmentsWithCities(HttpServletRequest request) {
        List<DepartmentResponseDTO> response = catalogQueryHandler.getAllDepartmentsWithCities();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Departamentos (con ciudades) obtenidos exitosamente", response);
    }

    /**
     * Lista todas las ciudades (sin incluir su departamento).
     */
    @Operation(
        summary = "Listar ciudades (sin departamento)",
        description = "Retorna todas las ciudades sin la información del departamento."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Catálogo vacío o error interno",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<CitySummaryResponseDTO>>> getAllCities(HttpServletRequest request) {
        List<CitySummaryResponseDTO> response = catalogQueryHandler.getAllCities();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Ciudades obtenidas exitosamente", response);
    }

    /**
     * Lista todas las ciudades incluyendo su departamento
     * (el Department no incluye sus cities).
     */
    @Operation(
        summary = "Listar ciudades con su departamento",
        description = "Retorna todas las ciudades con la información de su departamento (sin incluir las ciudades del departamento)."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Catálogo obtenido correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Catálogo vacío o error interno",
            content = @Content(schema = @Schema(implementation = ErrorResponseApiResponse.class))
        )
    })
    @GetMapping("/cities/with-department")
    public ResponseEntity<ApiResponse<List<CityResponseDTO>>> getAllCitiesWithDepartment(HttpServletRequest request) {
        List<CityResponseDTO> response = catalogQueryHandler.getAllCitiesWithDepartment();
        return ResponseUtil.ok(request, SuccessCode.RETRIEVED, "Ciudades (con departamento) obtenidas exitosamente", response);
    }
}
