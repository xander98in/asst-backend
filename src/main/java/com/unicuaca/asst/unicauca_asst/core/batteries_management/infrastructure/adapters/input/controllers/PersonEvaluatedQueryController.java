package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.query.PersonEvaluatedQueryHandler;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST que expone los endpoints de consulta relacionados con el agregado {@code Person}.
 *
 * Esta clase pertenece a la capa de infraestructura (adaptador de entrada) de la arquitectura hexagonal,
 * y se encarga de recibir las solicitudes HTTP desde el exterior y delegarlas a los casos de uso
 * definidos en la capa de aplicación.
 *
 * <p>Utiliza {@link PersonEvaluatedQueryHandler} como puerto de entrada para ejecutar la lógica de consulta
 * relacionada con personas.</p>
 */
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonEvaluatedQueryController {

    private final PersonEvaluatedQueryHandler personEvaluatedQueryHandler;

    /**
     * Endpoint para consultar la información de una persona por su identificador.
     *
     * @param idPersonEvaluated identificador único de la persona a consultar.
     * @return una {@link ResponseEntity} con un {@link ApiResponse} que contiene los datos de la persona
     *         en un objeto {@link PersonEvaluatedResponseDTO}, o un mensaje de error si no se encuentra.
     */
    @GetMapping("/query-by-id/{idPerson}")
    public ResponseEntity<ApiResponse<PersonEvaluatedResponseDTO>> queryPersonById(@PathVariable Long idPersonEvaluated) {
        ApiResponse<PersonEvaluatedResponseDTO> response = personEvaluatedQueryHandler.getPersonEvaluatedById(idPersonEvaluated);
        return ResponseEntity.ok(response);
    }

}
