package com.unicuaca.asst.unicauca_asst.core.reports.domain.services;

import java.time.LocalDateTime;
import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.models.Evaluator;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.input.EvaluatorCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.reports.domain.ports.output.EvaluatorQueryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la gestión de comandos de evaluadores.
 *
 * <p>Orquesta la creación, actualización y eliminación de evaluadores,
 * validando la propiedad (ownership) del usuario sobre el evaluador y
 * verificando que no esté en uso por ningún espacio de análisis antes
 * de eliminarlo.</p>
 */
@RequiredArgsConstructor
public class EvaluatorCommandService implements EvaluatorCommandCUInputPort {

    private final EvaluatorCommandRepository evaluatorCommandRepository;
    private final EvaluatorQueryRepository evaluatorQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea un nuevo evaluador asignando la fecha de creación actual.
     *
     * @param evaluator datos del evaluador a registrar
     * @return el evaluador persistido
     */
    @Override
    public Evaluator createEvaluator(Evaluator evaluator) {
        evaluator.setCreatedAt(LocalDateTime.now());
        return evaluatorCommandRepository.save(evaluator);
    }

    /**
     * Actualiza un evaluador existente previa validación de existencia y ownership.
     *
     * @param evaluatorId ID del evaluador a actualizar
     * @param evaluator   nuevos datos del evaluador
     * @param userId      ID del usuario que realiza la operación
     * @return el evaluador actualizado
     */
    @Override
    public Evaluator updateEvaluator(Long evaluatorId, Evaluator evaluator, Long userId) {
        Optional<Evaluator> optional = evaluatorQueryRepository.findById(evaluatorId);
        if (optional.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.EVALUATOR_NOT_FOUND,
                "user.report.evaluator_not_found",
                evaluatorId
            );
        }
        Evaluator existing = optional.get();

        if (!existing.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EVALUATOR_ACCESS_DENIED,
                "user.report.evaluator_access_denied",
                userId, evaluatorId
            );
        }

        existing.setFullName(evaluator.getFullName());
        existing.setIdentificationNumber(evaluator.getIdentificationNumber());
        existing.setProfession(evaluator.getProfession());
        existing.setPostgraduateDegree(evaluator.getPostgraduateDegree());
        existing.setProfessionalCardNumber(evaluator.getProfessionalCardNumber());
        existing.setOccupationalHealthLicense(evaluator.getOccupationalHealthLicense());
        existing.setLicenseIssueDate(evaluator.getLicenseIssueDate());

        return evaluatorCommandRepository.save(existing);
    }

    /**
     * Elimina un evaluador validando existencia, ownership y que no esté en uso.
     *
     * @param evaluatorId ID del evaluador a eliminar
     * @param userId      ID del usuario que realiza la operación
     */
    @Override
    public void deleteEvaluator(Long evaluatorId, Long userId) {
        Optional<Evaluator> optional = evaluatorQueryRepository.findById(evaluatorId);
        if (optional.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.EVALUATOR_NOT_FOUND,
                "user.report.evaluator_not_found",
                evaluatorId
            );
        }
        Evaluator existing = optional.get();

        if (!existing.getCreatorUserId().equals(userId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EVALUATOR_ACCESS_DENIED,
                "user.report.evaluator_access_denied",
                userId, evaluatorId
            );
        }

        if (evaluatorQueryRepository.isEvaluatorInUseByAnySpace(evaluatorId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.EVALUATOR_IN_USE,
                "user.report.evaluator_in_use",
                evaluatorId
            );
        }

        evaluatorCommandRepository.deleteById(evaluatorId);
    }
}
