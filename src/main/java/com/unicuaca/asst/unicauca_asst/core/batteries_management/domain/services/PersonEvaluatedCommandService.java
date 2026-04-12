package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.StatusPersonEvaluated;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.StatusPersonEvaluatedEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la gestión de comandos de personas evaluadas.
 * 
 * <p>Implementa la lógica de negocio para el registro, actualización y eliminación de personas.
 * Garantiza la integridad de los datos (unicidad de identificación y correo) y sincroniza
 * las reglas de negocio mediante el sistema de internacionalización (i18n).</p>
 */
@RequiredArgsConstructor
public class PersonEvaluatedCommandService implements PersonEvaluatedCommandCUInputPort {

    private final PersonEvaluatedCommandRepository personEvaluatedCommandRepository;
    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final CatalogQueryRepository catalogQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea una nueva persona evaluada en el sistema, validando previamente que no exista
     * otra con el mismo número de identificación o correo electrónico.
     *
     * @param personEvaluated modelo con los datos de la persona a crear
     * @return la persona evaluada creada con su ID asignado
     */
    @Override
    public PersonEvaluated createPersonEvaluated(PersonEvaluated personEvaluated) {
        personEvaluated.setFirstName(personEvaluated.getFirstName().toUpperCase());
        personEvaluated.setLastName(personEvaluated.getLastName().toUpperCase());

        // Validación de catálogo para el tipo de identificación
        Optional<IdentificationType> optionalIdType = catalogQueryRepository.getIdTypeByAbbreviation(personEvaluated.getIdentificationType().getAbbreviation());
        if (optionalIdType.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_ID_TYPE_NOT_FOUND,
                "user.person.id_type_invalid",
                personEvaluated.getIdentificationType().getAbbreviation()
            );
        }
        IdentificationType identificationType = optionalIdType.get();

        personEvaluated.setIdentificationType(identificationType);

        // Regla de Negocio: Unicidad de identificación
        if(personEvaluatedQueryRepository.existsByIdentification(personEvaluated.getIdentificationType().getId(), personEvaluated.getIdentificationNumber())) {
            this.resultFormatter.throwEntityAlreadyExists(
                ErrorCode.PERSON_IDENTIFICATION_EXISTS,
                "user.person.identification_exists",
                personEvaluated.getIdentificationNumber()
            );
        }

        // Regla de Negocio: Unicidad de correo electrónico
        if (personEvaluatedQueryRepository.existsByEmail(personEvaluated.getEmail())) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.PERSON_EMAIL_EXISTS,
                "user.person.email_exists",
                personEvaluated.getEmail()
            );
        }

        // Asignación de estado inicial por defecto
        Optional<StatusPersonEvaluated> optionalStatus = personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription());
        if (optionalStatus.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_STATUS_NOT_FOUND,
                "user.person.status_not_found",
                StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription()
            );
        }
        StatusPersonEvaluated statusPersonEvaluated = optionalStatus.get();
        personEvaluated.setStatus(statusPersonEvaluated);

        Optional<PersonEvaluated> optionalSaved = personEvaluatedCommandRepository.savePersonEvaluated(personEvaluated);
        if (optionalSaved.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.ENTITY_CREATION_ERROR,
                "user.person.creation_failed",
                personEvaluated.getIdentificationNumber()
            );
        }
        return optionalSaved.get();
    }

    /**
     * Actualiza la información de una persona evaluada existente.
     * 
     * Valida que la persona exista y que el nuevo correo no esté asignado a otra persona.
     *      * Si la persona no existe o el correo está duplicado, lanza una excepción personalizada.
     *
     * @param personEvaluated modelo con los datos actualizados e ID válido
     * @return la persona evaluada con los cambios persistidos
     */
    @Override
    public PersonEvaluated updatePersonEvaluated(PersonEvaluated personEvaluated) {
        Long id = personEvaluated.getId();
        personEvaluated.setFirstName(personEvaluated.getFirstName().toUpperCase());
        personEvaluated.setLastName(personEvaluated.getLastName().toUpperCase());

        // Verificación de existencia previa
        if (!personEvaluatedQueryRepository.existsById(id)) {
            this.resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_NOT_FOUND,
                "user.person.not_found",
                id
            );
        }

        Optional<IdentificationType> optionalIdType = catalogQueryRepository.getIdTypeByAbbreviation(personEvaluated.getIdentificationType().getAbbreviation());
        if (optionalIdType.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_ID_TYPE_NOT_FOUND,
                "user.person.id_type_invalid",
                personEvaluated.getIdentificationType().getAbbreviation()
            );
        }
        IdentificationType identificationType = optionalIdType.get();
        personEvaluated.setIdentificationType(identificationType);

        // Validación de conflictos de identidad con otros registros
        if(personEvaluatedQueryRepository.isIdentificationAssignedToDifferentPerson(personEvaluated.getIdentificationType().getId(), personEvaluated.getIdentificationNumber(), personEvaluated.getId())) {
            this.resultFormatter.throwEntityAlreadyExists(
                ErrorCode.PERSON_IDENTIFICATION_EXISTS,
                "user.person.update_identification_exists",
                personEvaluated.getIdentificationNumber()
            );
        }

        if (personEvaluatedQueryRepository.isEmailAssignedToDifferentPerson(personEvaluated.getEmail(), personEvaluated.getId())) {
            this.resultFormatter.throwEntityAlreadyExists(
                ErrorCode.PERSON_EMAIL_EXISTS,
                "user.person.update_email_exists",
                personEvaluated.getEmail()
            );
        }

        Optional<PersonEvaluated> optionalUpdated = personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated);
        if (optionalUpdated.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.ENTITY_UPDATE_ERROR,
                "user.person.update_failed",
                id
            );
        }
        return optionalUpdated.get();
    }

    /**
     * Elimina una persona evaluada del sistema.
     * 
     * <p>Regla de integridad: No se permite la eliminación si la persona posee registros
     * históricos de gestión de baterías asociados.</p>
     *
     * @param id identificador de la persona a eliminar
     */
    @Override
    public void deletePersonEvaluated(Long id) {

        if (!personEvaluatedQueryRepository.existsById(id)) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_NOT_FOUND,
                "user.person.not_found",
                id
            );
        }

        // Validación de integridad referencial (Regla de negocio)
        boolean hasBatteryRecords = batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(id);
        if (hasBatteryRecords) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.PERSON_WITH_BATTERY_RECORD,
                "user.person.has_records",
                id
            );
        }

        personEvaluatedCommandRepository.deletePersonEvaluatedById(id);
    }
}
