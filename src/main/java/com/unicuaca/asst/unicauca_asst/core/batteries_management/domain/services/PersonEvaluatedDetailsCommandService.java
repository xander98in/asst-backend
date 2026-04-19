package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.services;

import java.util.Optional;
import java.util.function.Supplier;

import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.ResultFormatterOutputPort;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.City;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.CivilStatus;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.ContractType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.EducationLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.Gender;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.HousingType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.JobPositionType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SalaryType;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.models.SocioeconomicLevel;
import com.unicuaca.asst.unicauca_asst.core.catalog.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.exceptions.structure.ErrorCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.BatteryManagementRecordStatusCode;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.enums.QuestionnaireEnum;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordStatusQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsCommandRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.PersonEvaluatedDetailsQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.QuestionnaireManagementRecordQueryRepository;

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

        Optional<PersonEvaluatedDetails> optionalSaved = personEvaluatedDetailsCommandRepository
            .savePersonEvaluatedDetails(personEvaluatedDetails);
        if (optionalSaved.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.ENTITY_CREATION_ERROR,
                "user.person_evaluated_details.creation_failed",
                bmrId
            );
        }

        syncBatteryToInProcessing(bmrId);
    }

    /**
     * Actualiza la información sociodemográfica existente.
     *
     * @param id identificador del detalle a actualizar
     * @param personEvaluatedDetails datos a persistir sobre el detalle existente
     */
    @Override
    public void updatePersonEvaluatedDetails(Long id, PersonEvaluatedDetails personEvaluatedDetails) {
        Optional<PersonEvaluatedDetails> optionalExisting = personEvaluatedDetailsQueryRepository
            .getByIdWithAll(id);
        if (optionalExisting.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_DETAILS_NOT_FOUND,
                "user.person_evaluated_details.update_not_found",
                id
            );
        }
        PersonEvaluatedDetails existing = optionalExisting.get();

        personEvaluatedDetails.setBatteryManagementRecord(existing.getBatteryManagementRecord());
        hydrateDetails(personEvaluatedDetails);
        personEvaluatedDetails.setId(existing.getId());

        Optional<PersonEvaluatedDetails> optionalUpdated = personEvaluatedDetailsCommandRepository
            .updatePersonEvaluatedDetails(id, personEvaluatedDetails);
        if (optionalUpdated.isEmpty()) {
            resultFormatter.throwEntityCreationFailed(
                ErrorCode.ENTITY_UPDATE_ERROR,
                "user.person_evaluated_details.update_failed",
                id
            );
        }
    }

    /**
     * Elimina los detalles sociodemográficos.
     *
     * <p>Regla de integridad: no se permite la eliminación si ya existen cuestionarios
     * intralaborales asociados a la batería contenedora.</p>
     *
     * @param personEvaluatedDetailsId identificador del detalle a eliminar
     */
    @Override
    public void deletePersonEvaluatedDetails(Long personEvaluatedDetailsId) {

        Optional<PersonEvaluatedDetails> optionalDetails = personEvaluatedDetailsQueryRepository
            .getByIdWithAll(personEvaluatedDetailsId);
        if (optionalDetails.isEmpty()) {
            this.resultFormatter.throwEntityNotFound(
                ErrorCode.PERSON_DETAILS_NOT_FOUND,
                "user.person_evaluated_details.delete_not_found",
                personEvaluatedDetailsId
            );
        }
        PersonEvaluatedDetails details = optionalDetails.get();

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
        Optional<BatteryManagementRecord> optionalRecord = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(bmrId);
        if (optionalRecord.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.BATTERY_RECORD_NOT_FOUND,
                "user.person_evaluated_details.battery_not_found",
                bmrId
            );
        }
        BatteryManagementRecord record = optionalRecord.get();

        Optional<BatteryManagementRecordStatus> optionalStatus = batteryManagementRecordStatusQueryRepository
            .getStatusByName(BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription());
        if (optionalStatus.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.BATTERY_STATUS_NOT_FOUND,
                "user.person_evaluated_details.sync_status_not_found",
                BatteryManagementRecordStatusCode.IN_PROCESSING.getDescription()
            );
        }
        BatteryManagementRecordStatus status = optionalStatus.get();

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
            Optional<BatteryManagementRecord> optionalRecord = batteryManagementRecordQueryRepository
                .getBatteryManagementRecordById(bmrId);
            if (optionalRecord.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_RECORD_NOT_FOUND,
                    "user.person_evaluated_details.battery_not_found",
                    bmrId
                );
            }
            BatteryManagementRecord record = optionalRecord.get();

            Optional<BatteryManagementRecordStatus> optionalStatus = batteryManagementRecordStatusQueryRepository
                .getStatusByName(BatteryManagementRecordStatusCode.CREATED.getDescription());
            if (optionalStatus.isEmpty()) {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.BATTERY_STATUS_NOT_FOUND,
                    "user.person_evaluated_details.sync_status_failed",
                    BatteryManagementRecordStatusCode.CREATED.getDescription()
                );
            }
            BatteryManagementRecordStatus status = optionalStatus.get();

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
        Long id = safeId(input.getId(), "registro de gestión de batería");
        return fetchOrThrow(() -> batteryManagementRecordQueryRepository.getBatteryManagementRecordById(id), "registro de gestión de batería", id);
    }

    /**
     * Resuelve y valida un género.
     *
     * @param input el género a resolver
     * @return el género resuelto
     */
    private Gender resolveGender(Gender input) {
        Long id = safeId(input.getId(), "género");
        return fetchOrThrow(() -> catalogQueryRepository.getGenderById(id), "género", id);
    }

    /**
     * Resuelve y valida un estado civil.
     *
     * @param input el estado civil a resolver
     * @return el estado civil resuelto
     */
    private CivilStatus resolveCivilStatus(CivilStatus input) {
        Long id = safeId(input.getId(), "estado civil");
        return fetchOrThrow(() -> catalogQueryRepository.getCivilStatusById(id), "estado civil", id);
    }

    /**
     * Resuelve y valida un nivel de educación.
     *
     * @param input el nivel de educación a resolver
     * @return el nivel de educación resuelto
     */
    private EducationLevel resolveEducationLevel(EducationLevel input) {
        Long id = safeId(input.getId(), "nivel de educación");
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
        Long id = safeId(input.getId(), "ciudad de " + contexto);
        return fetchOrThrow(() -> catalogQueryRepository.getCityById(id), "ciudad de " + contexto, id);
    }

    /**
     * Resuelve y valida un nivel socioeconómico.
     *
     * @param input el nivel socioeconómico a resolver
     * @return el nivel socioeconómico resuelto
     */
    private SocioeconomicLevel resolveSocioeconomicLevel(SocioeconomicLevel input) {
        Long id = safeId(input.getId(), "nivel socioeconómico");
        return fetchOrThrow(() -> catalogQueryRepository.getSocioeconomicLevelById(id), "nivel socioeconómico", id);
    }

    /**
     * Resuelve y valida un tipo de vivienda.
     *
     * @param input el tipo de vivienda a resolver
     * @return el tipo de vivienda resuelto
     */
    private HousingType resolveHousingType(HousingType input) {
        Long id = safeId(input.getId(), "tipo de vivienda");
        return fetchOrThrow(() -> catalogQueryRepository.getHousingTypeById(id), "tipo de vivienda", id);
    }

    /**
     * Resuelve y valida un tipo de cargo.
     *
     * @param input el tipo de cargo a resolver
     * @return el tipo de cargo resuelto
     */
    private JobPositionType resolveJobPositionType(JobPositionType input) {
        Long id = safeId(input.getId(), "tipo de cargo");
        return fetchOrThrow(() -> catalogQueryRepository.getJobPositionTypeById(id), "tipo de cargo", id);
    }

    /**
     * Resuelve y valida un tipo de contrato.
     *
     * @param input el tipo de contrato a resolver
     * @return el tipo de contrato resuelto
     */
    private ContractType resolveContractType(ContractType input) {
        Long id = safeId(input.getId(), "tipo de contrato");
        return fetchOrThrow(() -> catalogQueryRepository.getContractTypeById(id), "tipo de contrato", id);
    }

    /**
     * Resuelve y valida un tipo de salario.
     *
     * @param input el tipo de salario a resolver
     * @return el tipo de salario resuelto
     */
    private SalaryType resolveSalaryType(SalaryType input) {
        Long id = safeId(input.getId(), "tipo de salario");
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
        Optional<T> result = fetcher.get();
        if (result.isEmpty()) {
            resultFormatter.throwEntityNotFound(
                ErrorCode.CATALOG_RESOURCE_NOT_FOUND,
                "user.person_evaluated_details.catalog_invalid",
                recurso,
                id
            );
        }
        return result.get();
    }

    /**
     * Valida que un ID de catálogo no sea nulo.
     *
     * @param id     el ID a validar
     * @param nombre nombre legible del recurso (para el mensaje de error)
     * @return el ID validado
     */
    private Long safeId(Long id, String nombre) {
        if (id == null) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.BAD_REQUEST,
                "user.person_evaluated_details.required_fields_missing",
                nombre
            );
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
