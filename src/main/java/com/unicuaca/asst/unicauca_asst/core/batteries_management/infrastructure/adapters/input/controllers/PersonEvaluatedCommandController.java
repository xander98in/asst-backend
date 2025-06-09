package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicuaca.asst.unicauca_asst.common.response.ApiResponse;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.command.PersonEvaluatedCommandHandler;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedCreateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.request.PersonEvaluatedUpdateRequestDTO;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.application.dto.response.PersonEvaluatedResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestionar operaciones de creación de personas evaluadas.
 * 
 * Forma parte de la capa de entrada (Input Adapter) en la arquitectura hexagonal.
 * Delega la lógica al {@link PersonEvaluatedCommandHandler}.
 */
@RestController
@RequestMapping("/person-evaluated")
@RequiredArgsConstructor
public class PersonEvaluatedCommandController {

    private final PersonEvaluatedCommandHandler personEvaluatedCommandHandler;

    /**
     * Crea una nueva persona evaluada en el sistema.
     *
     * @param dto Datos de entrada validados para la creación de una persona.
     * @return Respuesta con el objeto creado y estado HTTP 201 (Created).
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PersonEvaluatedResponseDTO>> create(@Valid @RequestBody PersonEvaluatedCreateRequestDTO dto) {
        ApiResponse<PersonEvaluatedResponseDTO> response = personEvaluatedCommandHandler.createPersonEvaluated(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint REST para actualizar una persona evaluada.
     *
     * @param id ID de la persona evaluada
     * @param dto cuerpo de la solicitud con los datos nuevos
     * @return respuesta HTTP con el objeto actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<PersonEvaluatedResponseDTO>> update(@PathVariable Long id, @Valid @RequestBody PersonEvaluatedUpdateRequestDTO dto) {
        ApiResponse<PersonEvaluatedResponseDTO> response = personEvaluatedCommandHandler.updatePersonEvaluated(id, dto);
        return ResponseEntity.ok(response);
    }
}
