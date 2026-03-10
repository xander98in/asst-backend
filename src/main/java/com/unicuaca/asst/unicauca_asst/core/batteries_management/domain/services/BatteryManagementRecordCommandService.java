package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.*;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireManagementRecordStatusEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.StatusPersonEvaluatedEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.BatteryManagementRecordCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BatteryManagementRecordCommandService implements BatteryManagementRecordCommandCUInputPort {

    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final PersonEvaluatedQueryRepository personEvaluatedQueryRepository;
    private final PersonEvaluatedCommandRepository personEvaluatedCommandRepository;
    private final QuestionnaireManagementRecordCommandRepository questionnaireManagementRecordCommandRepository;
    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final QuestionnaireManagementRecordStatusQueryRepository questionnaireManagementRecordStatusQueryRepository;
    private final ResultFormatterOutputPort resultFormatterOutputPort;

    /**
     * Crea un nuevo registro de gestión de baterías para la persona evaluada indicada.
     *
     * @param personEvaluatedId ID de la persona evaluada para la cual se crea el registro.
     * @return El registro de gestión de baterías creado.
     */
    @Override
    public BatteryManagementRecord createBatteryManagementRecord(Long personEvaluatedId) {

        // Se crea una nueva instancia de BatteryManagementRecord
        BatteryManagementRecord record = BatteryManagementRecord.builder()
            .id(null)
            .status(null)
            .personEvaluated(null)
            .build();

        // Se busca el estado "Creado" para asignarlo al registro de gestión de baterías
        BatteryManagementRecordStatus batteryManagementRecordStatus = batteryManagementRecordQueryRepository.getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CREATED.getDescription())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Creado' no fue encontrado")
                );
                return null;
            });
        record.setStatus(batteryManagementRecordStatus);

        // Se busca la persona evaluada por su ID se actualiza su estado a "Con registro" y se asigna al registro de gestión de baterías
        PersonEvaluated personEvaluated = null;
        if(personEvaluatedQueryRepository.existsById(personEvaluatedId)) {
            personEvaluated = personEvaluatedQueryRepository.getPersonEvaluatedById(personEvaluatedId)
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " no fue encontrada")
                    );
                    return null;
                });
            StatusPersonEvaluated statusPersonEvaluated = personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(StatusPersonEvaluatedEnum.WITH_RECORD.getDescription())
                .orElseGet(() -> {
                    this.resultFormatterOutputPort.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Con registro' no fue encontrado.")
                    );
                    return null;
                });

            if(personEvaluated != null) {
                personEvaluated.setStatus(statusPersonEvaluated);
                String identificationNumber = personEvaluated.getIdentificationNumber();
                personEvaluated = personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated)
                    .orElseGet(() -> {
                        this.resultFormatterOutputPort.throwEntityCreationFailed(
                            ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                            String.format(ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(), "La persona con ID " + identificationNumber + " no se actualizó correctamente.")
                        );
                        return null;
                    });
            }
            System.out.println("Persona evaluada encontrada: " + personEvaluated);
            record.setPersonEvaluated(personEvaluated);
        }
        else {
            this.resultFormatterOutputPort.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " no fue encontrada")
            );
        }

        // Verifica si la persona evaluada ya tiene un registro de gestión de baterías
        if (batteryManagementRecordQueryRepository.existsByPersonEvaluatedId(personEvaluatedId)) {
            this.resultFormatterOutputPort.throwEntityAlreadyExists(
                ErrorCode.ENTITY_ALREADY_EXISTS.getCode(),
                String.format(ErrorCode.ENTITY_ALREADY_EXISTS.getMessageKey(), "La persona evaluada con ID " + personEvaluatedId + " ya tiene un registro de gestión de baterías.")
            );
        }

        // Guarda y retorna el nuevo registro de gestión de baterías
        return batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    String.format(ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(), "No fue posible crear el registro de gestión de baterías.")
                );
                return null;
            });
    }

    /**
     * Elimina un registro de gestión de baterías por su ID.
     *
     * @param id ID del registro de gestión de baterías a eliminar.
     */
    @Override
    public void deleteBatteryManagementRecord(Long id) {

        BatteryManagementRecord batteryManagementRecord = this.batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(id)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de baterías con ID " + id + " no fue encontrado.")
                );
                return null;
            });

        BatteryManagementRecordStatus status = batteryManagementRecord.getStatus();

        if (status.getName().equals(BatteryManagementRecordStatusCode.CREATED.getDescription())) {
            batteryManagementRecordCommandRepository.deleteBatteryManagementRecordById(id);

            // Se obtiene la persona evaluada asociada al registro eliminado
            PersonEvaluated personEvaluated = batteryManagementRecord.getPersonEvaluated();

            if (personEvaluated != null) {
                // Se consulta si la persona tiene otros registros en estado 'Creado', 'En diligenciamiento' o 'Diligenciado'
                boolean hasOtherActiveRecords = batteryManagementRecordQueryRepository.existsByPersonEvaluatedIdAndStatusNameIn(
                    personEvaluated.getId(),
                    List.of(
                        BatteryManagementRecordStatusCode.CREATED.getDescription(),
                        BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription(),
                        BatteryManagementRecordStatusCode.COMPLETED.getDescription()
                    )
                );

                String targetStatusName = hasOtherActiveRecords
                    ? StatusPersonEvaluatedEnum.WITH_RECORD.getDescription()
                    : StatusPersonEvaluatedEnum.WITHOUT_RECORD.getDescription();

                // Si el estado actual es diferente al objetivo, se actualiza
                if (!personEvaluated.getStatus().getName().equals(targetStatusName)) {
                    StatusPersonEvaluated newStatus = personEvaluatedQueryRepository.getStatusPersonEvaluatedByName(targetStatusName)
                        .orElseGet(() -> {
                            this.resultFormatterOutputPort.throwEntityNotFound(
                                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                                String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado '" + targetStatusName + "' no fue encontrado.")
                            );
                            return null;
                        });

                    personEvaluated.setStatus(newStatus);
                    personEvaluatedCommandRepository.updatePersonEvaluated(personEvaluated);
                }
            }
        } else {
            this.resultFormatterOutputPort.throwBusinessRuleViolation(
                ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD.getCode(),
                String.format(ErrorCode.DELETE_BATTERY_MANAGEMENT_RECORD.getMessageKey(), status.getName())
            );
        }
    }

    /**
     * Cierra un registro de gestión de baterías por su ID.
     *
     * @param recordId ID del registro de gestión de baterías a cerrar.
     * @return El registro de gestión de baterías cerrado.
     */
    @Override
    public BatteryManagementRecord closeBatteryManagementRecord(Long recordId) {
        // Obtener el registro de gestión de baterías
        BatteryManagementRecord record = batteryManagementRecordQueryRepository.getBatteryManagementRecordById(recordId)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El registro de gestión de baterías con ID " + recordId + " no fue encontrado.")
                );
                return null;
            });

        // 2. Verificar que el estado actual sea 'Diligenciado'
        if (!record.getStatus().getName().equals(BatteryManagementRecordStatusCode.COMPLETED.getDescription())) {
            this.resultFormatterOutputPort.throwBusinessRuleViolation(
                ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD.getCode(),
                String.format(ErrorCode.CLOSE_BATTERY_MANAGEMENT_RECORD.getMessageKey(), "'" + record.getStatus().getName() + "'")
            );
        }

        // Obtener el estado 'Cerrado' para cuestionarios
        QuestionnaireManagementRecordStatus closedQuestionnaireStatus = questionnaireManagementRecordStatusQueryRepository
            .getQuestionnaireManagementRecordStatusByName(QuestionnaireManagementRecordStatusEnum.CERRADO.getName())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Cerrado' para registros de cuestionarios no fue encontrado.")
                );
                return null;
            });

        // Actualizar todos los registros de cuestionarios asociados a 'Cerrado'
        List<QuestionnaireManagementRecord> questionnaires = questionnaireManagementRecordQueryRepository.findAllByBatteryManagementRecordId(recordId);
        questionnaires.forEach(q -> {
            q.setStatus(closedQuestionnaireStatus);
            questionnaireManagementRecordCommandRepository.save(q);
        });

        // Obtener el estado 'Cerrado' para el registro de gestión de batería
        BatteryManagementRecordStatus closedBatteryStatus = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordStatudByName(BatteryManagementRecordStatusCode.CLOSED.getDescription())
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), "El estado 'Cerrado' para registros de gestión de batería no fue encontrado.")
                );
                return null;
            });

        // Actualizar el estado del registro de batería a 'Cerrado'
        record.setStatus(closedBatteryStatus);
        return batteryManagementRecordCommandRepository.saveBatteryManagementRecord(record)
            .orElseGet(() -> {
                this.resultFormatterOutputPort.throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    String.format(ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(), "No fue posible cerrar el registro de gestión de batería.")
                );
                return null;
            });
    }
}
