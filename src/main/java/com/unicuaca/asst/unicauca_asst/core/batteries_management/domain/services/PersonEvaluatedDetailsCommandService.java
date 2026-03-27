package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.Optional;
import java.util.function.Supplier;

import com.unicuaca.asst.unicauca_asst.common.application.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.common.domain.models.*;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de dominio para la gestión de comandos de detalles sociodemográficos.
 * 
 * <p>Maneja la persistencia y validación de la información sociodemográfica de las personas evaluadas.
 * Coordina la resolución de catálogos y sincroniza el estado del proceso de evaluación (Batería)
 * tras cada operación.</p>
 */
@RequiredArgsConstructor
public class PersonEvaluatedDetailsCommandService implements PersonEvaluatedDetailsCommandCUInputPort {

    private final CatalogQueryRepository catalogQueryRepository;
    private final BatteryManagementRecordQueryRepository batteryManagementRecordQueryRepository;
    private final PersonEvaluatedDetailsCommandRepository personEvaluatedDetailsCommandRepository;
    private final PersonEvaluatedDetailsQueryRepository personEvaluatedDetailsQueryRepository;
    private final QuestionnaireManagementRecordQueryRepository questionnaireManagementRecordQueryRepository;
    private final BatteryManagementRecordCommandRepository batteryManagementRecordCommandRepository;
    private final BatteryManagementRecordStatusQueryRepository batteryManagementRecordStatusQueryRepository;
    private final ResultFormatterOutputPort resultFormatter;

    /**
     * Crea un nuevo registro de detalles sociodemográficos para una batería.
     * 
     * @param personEvaluatedDetails datos a crear
     */
    @Override
    public void createPersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails) {

        personEvaluatedDetails.setBatteryManagementRecord(resolveBatteryRecord(personEvaluatedDetails.getBatteryManagementRecord()));

        Long bmrId = personEvaluatedDetails.getBatteryManagementRecord().getId();
        if (personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(bmrId)) {
            resultFormatter.throwEntityAlreadyExists(
                ErrorCode.PERSON_DETAILS_ALREADY_EXISTS,
                "user.person_evaluated_details.already_exists",
                bmrId
            );
        }

        hydrateDetails(personEvaluatedDetails);

        personEvaluatedDetailsCommandRepository
            .savePersonEvaluatedDetails(personEvaluatedDetails)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR,
                    "user.person_evaluated_details.creation_failed",
                    bmrId
                );
                return null;
            });

        syncBatteryToInProcessing(bmrId);
    }

    /**
     * Actualiza la información sociodemográfica existente.
     */
    @Override
    public void updatePersonEvaluatedDetails(Long id, PersonEvaluatedDetails personEvaluatedDetails) {
        PersonEvaluatedDetails existing = personEvaluatedDetailsQueryRepository
            .getByIdWithAll(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.update_not_found",
                    id
                );
                return null;
            });

        personEvaluatedDetails.setBatteryManagementRecord(existing.getBatteryManagementRecord());
        hydrateDetails(personEvaluatedDetails);
        personEvaluatedDetails.setId(existing.getId());

        personEvaluatedDetailsCommandRepository
            .updatePersonEvaluatedDetails(id, personEvaluatedDetails)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR,
                    "user.person_evaluated_details.update_failed",
                    id
                );
                return null;
            });
    }

    /**
     * Elimina los detalles sociodemográficos.
     */
    @Override
    public void deletePersonEvaluatedDetails(Long personEvaluatedDetailsId) {

        PersonEvaluatedDetails details = personEvaluatedDetailsQueryRepository
            .getByIdWithAll(personEvaluatedDetailsId)
            .orElseGet(() -> {
                this.resultFormatter.throwEntityNotFound(
                    ErrorCode.PERSON_DETAILS_NOT_FOUND,
                    "user.person_evaluated_details.delete_not_found",
                    personEvaluatedDetailsId
                );
                return null;
            });

        Long batteryRecordId = details.getBatteryManagementRecord().getId();

        boolean hasIntralaboralRecords = questionnaireManagementRecordQueryRepository
            .existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                batteryRecordId,
                java.util.List.of(QuestionnaireEnum.ILA.getAbbreviation(), QuestionnaireEnum.ILB.getAbbreviation())
            );

        if (hasIntralaboralRecords) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED,
                "user.person_evaluated_details.delete_not_allowed",
                batteryRecordId
            );
            return;
        }

        personEvaluatedDetailsCommandRepository.deleteById(personEvaluatedDetailsId);
        syncBatteryAfterDetailsDelete(batteryRecordId);
    }

    /* ========================== Helpers de Hidratación ========================== */

    /**
     * Hidratación de campos relacionados con catálogos y normalización de texto.
     *
     * <p>Este método centraliza la lógica de resolución de catálogos y normalización de campos
     * para asegurar consistencia en la creación y actualización de detalles sociodemográficos.</p>
     *
     * @param details el objeto de detalles a hidratar
     */
    private void hydrateDetails(PersonEvaluatedDetails details) {
        details.setGender(resolveGender(details.getGender()));
        details.setCivilStatus(resolveCivilStatus(details.getCivilStatus()));
        details.setEducationLevel(resolveEducationLevel(details.getEducationLevel()));
        details.setProfession(normalizeText(details.getProfession()));
        details.setResidenceCity(resolveCity(details.getResidenceCity(), "residencia"));
        details.setSocioeconomicLevel(resolveSocioeconomicLevel(details.getSocioeconomicLevel()));
        details.setHousingType(resolveHousingType(details.getHousingType()));
        details.setWorkCity(resolveCity(details.getWorkCity(), "trabajo"));
        details.setYearsAtCompany(normalizeText(details.getYearsAtCompany()));
        details.setJobTitle(normalizeText(details.getJobTitle()));
        details.setJobPositionType(resolveJobPositionType(details.getJobPositionType()));
        details.setYearsInPosition(normalizeText(details.getYearsInPosition()));
        details.setWorkAreaName(normalizeText(details.getWorkAreaName()));
        details.setContractType(resolveContractType(details.getContractType()));
        details.setSalaryType(resolveSalaryType(details.getSalaryType()));
    }

    /**
     * Sincroniza el estado del registro de gestión de batería a "En Proceso" tras la creación o actualización de detalles.
     *
     * <p>Este método asegura que el proceso de evaluación avance al estado correcto una vez que se han registrado los detalles sociodemográficos.</p>
     *
     * @param bmrId el ID del registro de gestión de batería a sincronizar
     */
    private void syncBatteryToInProcessing(Long bmrId) {
        BatteryManagementRecord record = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(bmrId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.person_evaluated_details.battery_not_found",
                    bmrId
                );
                return null;
            });

        BatteryManagementRecordStatus status = batteryManagementRecordStatusQueryRepository
            .getStatusByName(BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription())
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.person_evaluated_details.sync_status_not_found",
                    BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()
                );
                return null;
            });

        record.setStatus(status);
        batteryManagementRecordCommandRepository.updateBatteryManagementRecord(record);
    }

    /**
     * Sincroniza el estado del registro de gestión de batería a "Creado" tras la eliminación de detalles, si no hay cuestionarios asociados.
     *
     * <p>Este método asegura que el proceso de evaluación retroceda al estado inicial si se eliminan los detalles sociodemográficos y no hay cuestionarios respondidos.</p>
     *
     * @param bmrId el ID del registro de gestión de batería a sincronizar
     */
    private void syncBatteryAfterDetailsDelete(Long bmrId) {
        if (!questionnaireManagementRecordQueryRepository.existsByBatteryManagementRecordId(bmrId)) {
            BatteryManagementRecord record = batteryManagementRecordQueryRepository
                .getBatteryManagementRecordById(bmrId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.BATTERY_RECORD_NOT_FOUND,
                        "user.person_evaluated_details.battery_not_found",
                        bmrId
                    );
                    return null;
                });

            BatteryManagementRecordStatus status = batteryManagementRecordStatusQueryRepository
                .getStatusByName(BatteryManagementRecordStatusCode.CREATED.getDescription())
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.BATTERY_STATUS_NOT_FOUND,
                        "user.person_evaluated_details.sync_status_failed",
                        BatteryManagementRecordStatusCode.CREATED.getDescription()
                    );
                    return null;
                });

            record.setStatus(status);
            batteryManagementRecordCommandRepository.updateBatteryManagementRecord(record);
        }
    }

    /* ========================== Helpers de Resolución ========================== */

    /**
     * Resuelve y valida un registro de gestión de batería.
     *
     * @param input el registro de gestión de batería a resolver
     * @return el registro resuelto
     */
    private BatteryManagementRecord resolveBatteryRecord(BatteryManagementRecord input) {
        Long id = safeId(input, input::getId, "registro de gestión de batería");
        return fetchOrThrow(() -> batteryManagementRecordQueryRepository.getBatteryManagementRecordById(id), "registro de gestión de batería", id);
    }

    /**
     * Resuelve y valida un género.
     *
     * @param input el género a resolver
     * @return el género resuelto
     */
    private Gender resolveGender(Gender input) {
        Long id = safeId(input, input::getId, "género");
        return fetchOrThrow(() -> catalogQueryRepository.getGenderById(id), "género", id);
    }

    /**
     * Resuelve y valida un estado civil.
     *
     * @param input el estado civil a resolver
     * @return el estado civil resuelto
     */
    private CivilStatus resolveCivilStatus(CivilStatus input) {
        Long id = safeId(input, input::getId, "estado civil");
        return fetchOrThrow(() -> catalogQueryRepository.getCivilStatusById(id), "estado civil", id);
    }

    /**
     * Resuelve y valida un nivel de educación.
     *
     * @param input el nivel de educación a resolver
     * @return el nivel de educación resuelto
     */
    private EducationLevel resolveEducationLevel(EducationLevel input) {
        Long id = safeId(input, input::getId, "nivel de educación");
        return fetchOrThrow(() -> catalogQueryRepository.getEducationLevelById(id), "nivel de educación", id);
    }

    /**
     * Resuelve y valida una ciudad.
     *
     * @param input el objeto ciudad a resolver
     * @param contexto contexto descriptivo para mensajes de error
     * @return la ciudad resuelta
     */
    private City resolveCity(City input, String contexto) {
        Long id = safeId(input, input::getId, "ciudad de " + contexto);
        return fetchOrThrow(() -> catalogQueryRepository.getCityById(id), "ciudad de " + contexto, id);
    }

    /**
     * Resuelve y valida un nivel socioeconómico.
     *
     * @param input el nivel socioeconómico a resolver
     * @return el nivel socioeconómico resuelto
     */
    private SocioeconomicLevel resolveSocioeconomicLevel(SocioeconomicLevel input) {
        Long id = safeId(input, input::getId, "nivel socioeconómico");
        return fetchOrThrow(() -> catalogQueryRepository.getSocioeconomicLevelById(id), "nivel socioeconómico", id);
    }

    /**
     * Resuelve y valida un tipo de vivienda.
     *
     * @param input el tipo de vivienda a resolver
     * @return el tipo de vivienda resuelto
     */
    private HousingType resolveHousingType(HousingType input) {
        Long id = safeId(input, input::getId, "tipo de vivienda");
        return fetchOrThrow(() -> catalogQueryRepository.getHousingTypeById(id), "tipo de vivienda", id);
    }

    /**
     * Resuelve y valida un tipo de cargo.
     *
     * @param input el tipo de cargo a resolver
     * @return el tipo de cargo resuelto
     */
    private JobPositionType resolveJobPositionType(JobPositionType input) {
        Long id = safeId(input, input::getId, "tipo de cargo");
        return fetchOrThrow(() -> catalogQueryRepository.getJobPositionTypeById(id), "tipo de cargo", id);
    }

    /**
     * Resuelve y valida un tipo de contrato.
     *
     * @param input el tipo de contrato a resolver
     * @return el tipo de contrato resuelto
     */
    private ContractType resolveContractType(ContractType input) {
        Long id = safeId(input, input::getId, "tipo de contrato");
        return fetchOrThrow(() -> catalogQueryRepository.getContractTypeById(id), "tipo de contrato", id);
    }

    /**
     * Resuelve y valida un tipo de salario.
     *
     * @param input el tipo de salario a resolver
     * @return el tipo de salario resuelto
     */
    private SalaryType resolveSalaryType(SalaryType input) {
        Long id = safeId(input, input::getId, "tipo de salario");
        return fetchOrThrow(() -> catalogQueryRepository.getSalaryTypeById(id), "tipo de salario", id);
    }

    /**
     * Helper genérico para resolver una entidad a partir de un fetcher que devuelve un Optional.
     * Si el Optional está vacío, lanza una excepción de entidad no encontrada con i18n.
     *
     * @param fetcher función que realiza la consulta y devuelve un Optional con la entidad
     * @param recurso nombre del recurso para mensajes de error (ej. "género", "ciudad de residencia")
     * @param id identificador del recurso para mensajes de error
     * @return la entidad resuelta o null si no se encuentra (la excepción ya se lanza en ese caso)
     */
    private <T> T fetchOrThrow(Supplier<Optional<T>> fetcher, String recurso, Long id) {
        return fetcher.get().orElseGet(() -> {
            resultFormatter.throwEntityNotFound(
                ErrorCode.CATALOG_RESOURCE_NOT_FOUND,
                "user.person_evaluated_details.catalog_invalid",
                recurso,
                id
            );
            return null;
        });
    }

    /**
     * Extrae de manera segura el ID de un objeto y valida nulls de forma genérica.
     *
     * <p>Este helper evita NullPointerException verificando que tanto el objeto como su ID sean válidos.</p>
     *
     * @param ref         referencia al objeto (puede ser null)
     * @param idSupplier  función que obtiene el ID del objeto
     * @param nombre      nombre legible del recurso (para el mensaje de error)
     * @return el ID no nulo del recurso
     */
    private Long safeId(Object ref, Supplier<Long> idSupplier, String nombre) {
        Long id = (ref != null) ? idSupplier.get() : null;
        if (id == null) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.BAD_REQUEST,
                "user.person_evaluated_details.required_fields_missing",
                nombre
            );
            return null;
        }
        return id;
    }

    /**
     * Normaliza un texto aplicando trim y capitalización de la primera letra.
     *
     * <p>Este helper mejora la consistencia de los datos al almacenar textos con formato uniforme.</p>
     *
     * @param value el texto a normalizar
     * @return el texto normalizado o el valor original si es null o blank
     */
    private String normalizeText(String value) {
        if (value == null || value.isBlank()) return value;
        value = value.trim();
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
