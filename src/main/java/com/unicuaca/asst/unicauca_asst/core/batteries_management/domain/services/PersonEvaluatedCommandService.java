package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityAlreadyExistsException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.EntityNotFoundPersException;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para operaciones que gestiona la creación de personas evaluadas.
 * 
 * Esta clase pertenece a la capa de dominio y orquesta la lógica de negocio para la creación de personas evaluadas.
 * Valida reglas de negocio como la no duplicidad antes de persistir.
 */
@RequiredArgsConstructor
public class PersonEvaluatedCommandService implements PersonEvaluatedCommandCUInputPort {

    private final PersonEvaluatedCommandRepository personEvaluatedCommandRepository;
    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea una nueva persona evaluada en el sistema, validando previamente que no exista
     * otra con el mismo número de identificación o correo electrónico.
     *
     * <p>Si la operación de guardado falla y no se retorna una instancia válida, lanza una excepción
     * de tipo {@code EntityNotFoundPersException} para indicar que la operación de persistencia no fue exitosa.</p>
     *
     * @param personEvaluated modelo de dominio con los datos de la persona a crear
     * @return la persona evaluada creada, con su identificador asignado por el sistema
     * @throws EntityAlreadyExistsException si ya existe una persona con el mismo número de identificación o correo electrónico
     * @throws EntityNotFoundPersException si la creación falla inesperadamente
     */
    @Override
    public PersonEvaluated createPersonEvaluated(PersonEvaluated personEvaluated) {
        if(personEvaluatedQueryRepository.existsByIdentification(personEvaluated.getIdentificationType().getId(), personEvaluated.getIdentificationNumber())) {
            this.resultFormatter.throwEntityAlreadyExists("La persona " + personEvaluated.getFirstName() + " se encuentra registrada.");
        }
        if (personEvaluatedQueryRepository.existsByEmail(personEvaluated.getEmail())) {
            resultFormatter.throwEntityAlreadyExists(ErrorCode.EMAIL_ALREADY_EXISTS, "El correo " + personEvaluated.getEmail() + " ya está registrado.");
        }
        return personEvaluatedCommandRepository.savePersonEvaluated(personEvaluated)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed("La persona no se creó correctamente");
                return null; // nunca se ejecuta, pero requerido por el compilador
            });
    }

    /**
     * Actualiza una persona evaluada en el sistema.
     * 
     * Valida que la persona exista y que el nuevo correo no esté asignado a otra persona.
     * Si la persona no existe o el correo está duplicado, lanza una excepción personalizada.
     *
     * @param personEvaluated objeto con los datos actualizados
     * @return objeto actualizado desde la capa de persistencia
     */
    @Override
    public PersonEvaluated updatePersonEvaluated(PersonEvaluated personEvaluated) {
        Long id = personEvaluated.getId();
        if (!personEvaluatedQueryRepository.existsById(id)) {
            resultFormatter.throwEntityNotFound("La persona con ID " + id + " no existe.");
        }
        if (personEvaluatedQueryRepository.existsByEmailAndIdNot(personEvaluated.getEmail(), personEvaluated.getId())) {
            resultFormatter.throwEntityAlreadyExists(ErrorCode.EMAIL_ALREADY_EXISTS, "El correo " + personEvaluated.getEmail() + " ya está registrado por otra persona.");
        }

        // Actualizar y retornar
        return personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed("Error al actualizar la persona evaluada");
                return null;
            });
    }
}
