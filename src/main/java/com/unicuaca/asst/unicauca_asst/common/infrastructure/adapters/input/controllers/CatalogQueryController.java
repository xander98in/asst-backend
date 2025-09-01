package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.application.dto.response.IdentificationTypeResponseDTO;
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
    public ResponseEntity<ApiResponse<List<IdentificationTypeResponseDTO>>> getIdTypes() {
        ApiResponse<List<IdentificationTypeResponseDTO>> response = catalogQueryHandler.getIdTypes();
        return ResponseEntity.ok(response);
    }

}
