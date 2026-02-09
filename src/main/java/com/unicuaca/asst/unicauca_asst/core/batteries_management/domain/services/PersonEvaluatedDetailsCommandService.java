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
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input.PersonEvaluatedDetailsCommandCUInputPort;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.*;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para operaciones que gestiona la creación de detalles
 * adicionales de una persona evaluada.
 * 
 * Esta clase pertenece a la capa de dominio y orquesta la lógica de negocio para la creación de detalles
 * de persona evaluada.
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
     * Crea los detalles de una persona evaluada.
     * 
     * @param personEvaluatedDetails el objeto {@link PersonEvaluatedDetails} que contiene los datos a crear
     */
    @Override
    public void createPersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails) {

        System.out.println("\n\n\nDtos recibidos en el servicio: " + personEvaluatedDetails + "\n\n");

        personEvaluatedDetails.setBatteryManagementRecord(resolveBatteryRecord(personEvaluatedDetails.getBatteryManagementRecord()));

        Long bmrId = personEvaluatedDetails.getBatteryManagementRecord().getId();
        if (personEvaluatedDetailsQueryRepository.existsByBatteryManagementRecordId(bmrId)) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.PERSON_EVALUATED_DETAILS_DUPLICATE.getCode(),
                String.format(ErrorCode.PERSON_EVALUATED_DETAILS_DUPLICATE.getMessageKey(), bmrId)
            );
        }

        personEvaluatedDetails.setGender(resolveGender(personEvaluatedDetails.getGender()));
        personEvaluatedDetails.setCivilStatus(resolveCivilStatus(personEvaluatedDetails.getCivilStatus()));
        personEvaluatedDetails.setEducationLevel(resolveEducationLevel(personEvaluatedDetails.getEducationLevel()));
        personEvaluatedDetails.setProfession(normalizeText(personEvaluatedDetails.getProfession()));
        personEvaluatedDetails.setResidenceCity(resolveCity(personEvaluatedDetails.getResidenceCity(), "residencia"));
        personEvaluatedDetails.setSocioeconomicLevel(resolveSocioeconomicLevel(personEvaluatedDetails.getSocioeconomicLevel()));
        personEvaluatedDetails.setHousingType(resolveHousingType(personEvaluatedDetails.getHousingType()));
        personEvaluatedDetails.setWorkCity(resolveCity(personEvaluatedDetails.getWorkCity(), "trabajo"));
        personEvaluatedDetails.setYearsAtCompany(normalizeText(personEvaluatedDetails.getYearsAtCompany()));
        personEvaluatedDetails.setJobTitle(normalizeText(personEvaluatedDetails.getJobTitle()));
        personEvaluatedDetails.setJobPositionType(resolveJobPositionType(personEvaluatedDetails.getJobPositionType()));
        personEvaluatedDetails.setYearsInPosition(normalizeText(personEvaluatedDetails.getYearsInPosition()));
        personEvaluatedDetails.setWorkAreaName(normalizeText(personEvaluatedDetails.getWorkAreaName()));
        personEvaluatedDetails.setContractType(resolveContractType(personEvaluatedDetails.getContractType()));
        personEvaluatedDetails.setSalaryType(resolveSalaryType(personEvaluatedDetails.getSalaryType()));

        System.out.println("\n\n\nDtos validados y completos en el servicio: " + personEvaluatedDetails + "\n\n");

        personEvaluatedDetailsCommandRepository
            .savePersonEvaluatedDetails(personEvaluatedDetails)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed(
                    ErrorCode.ENTITY_CREATION_ERROR.getCode(),
                    String.format(ErrorCode.ENTITY_CREATION_ERROR.getMessageKey(), "Los detalles de la persona evaluada no se crearon correctamente.")
                );
                return null; // requerido por el compilador
            });

        BatteryManagementRecord record = batteryManagementRecordQueryRepository
            .getBatteryManagementRecordById(bmrId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El registro de gestión de batería con ID " + bmrId + " no fue encontrado."
                    )
                );
                return null;
            });

        BatteryManagementRecordStatus createdStatus = batteryManagementRecordStatusQueryRepository
            .getStatusByName("En diligenciamiento")
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "El estado 'En diligenciamiento' no fue encontrado."
                    )
                );
                return null;
            });

        record.setStatus(createdStatus);

        batteryManagementRecordCommandRepository
            .updateBatteryManagementRecord(record)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    String.format(
                        ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(),
                        "No se pudo actualizar el estado del registro de gestión de batería con ID " + bmrId
                    )
                );
                return null;
            });
    }

    /**
     * Actualiza los detalles de una persona evaluada por su ID.
     *
     * @param id ID del detalle
     * @param personEvaluatedDetails datos a actualizar
     */
    @Override
    public void updatePersonEvaluatedDetails(Long id, PersonEvaluatedDetails personEvaluatedDetails) {
        // Validar que existe el detalle
        PersonEvaluatedDetails existing = personEvaluatedDetailsQueryRepository
            .getByIdWithAll(id)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "Los detalles de la persona evaluada con ID " + id + " no fueron encontrados."
                    )
                );
                return null;
            });

        // Resolver catálogos (igual que create)
        personEvaluatedDetails.setBatteryManagementRecord(existing.getBatteryManagementRecord());
        personEvaluatedDetails.setGender(resolveGender(personEvaluatedDetails.getGender()));
        personEvaluatedDetails.setCivilStatus(resolveCivilStatus(personEvaluatedDetails.getCivilStatus()));
        personEvaluatedDetails.setEducationLevel(resolveEducationLevel(personEvaluatedDetails.getEducationLevel()));
        personEvaluatedDetails.setProfession(normalizeText(personEvaluatedDetails.getProfession()));
        personEvaluatedDetails.setResidenceCity(resolveCity(personEvaluatedDetails.getResidenceCity(), "residencia"));
        personEvaluatedDetails.setSocioeconomicLevel(resolveSocioeconomicLevel(personEvaluatedDetails.getSocioeconomicLevel()));
        personEvaluatedDetails.setHousingType(resolveHousingType(personEvaluatedDetails.getHousingType()));
        personEvaluatedDetails.setWorkCity(resolveCity(personEvaluatedDetails.getWorkCity(), "trabajo"));
        personEvaluatedDetails.setYearsAtCompany(normalizeText(personEvaluatedDetails.getYearsAtCompany()));
        personEvaluatedDetails.setJobTitle(normalizeText(personEvaluatedDetails.getJobTitle()));
        personEvaluatedDetails.setJobPositionType(resolveJobPositionType(personEvaluatedDetails.getJobPositionType()));
        personEvaluatedDetails.setYearsInPosition(normalizeText(personEvaluatedDetails.getYearsInPosition()));
        personEvaluatedDetails.setWorkAreaName(normalizeText(personEvaluatedDetails.getWorkAreaName()));
        personEvaluatedDetails.setContractType(resolveContractType(personEvaluatedDetails.getContractType()));
        personEvaluatedDetails.setSalaryType(resolveSalaryType(personEvaluatedDetails.getSalaryType()));
        // Asegurar ID a actualizar y preservar createdAt si lo manejas en dominio (opcional)
        personEvaluatedDetails.setId(existing.getId());

        // Persistir
        personEvaluatedDetailsCommandRepository
            .updatePersonEvaluatedDetails(id, personEvaluatedDetails)
            .orElseGet(() -> {
                resultFormatter.throwEntityCreationFailed(
                    ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                    String.format(ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(), "Los detalles de la persona evaluada no se actualizaron correctamente.")
                );
                return null;
            });
    }

    /**
     * Elimina los detalles de una persona evaluada por su ID.
     *
     * @param personEvaluatedDetailsId ID del detalle a eliminar
     */
    @Override
    public void deletePersonEvaluatedDetails(Long personEvaluatedDetailsId) {

        PersonEvaluatedDetails details = personEvaluatedDetailsQueryRepository
            .getByIdWithAll(personEvaluatedDetailsId)
            .orElseGet(() -> {
                resultFormatter.throwEntityNotFound(
                    ErrorCode.ENTITY_NOT_FOUND.getCode(),
                    String.format(
                        ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                        "Los detalles de la persona evaluada con ID " + personEvaluatedDetailsId + " no fueron encontrados."
                    )
                );
                return null;
            });

        Long batteryRecordId = details.getBatteryManagementRecord().getId();

        boolean hasIntralaboralRecords = questionnaireManagementRecordQueryRepository
            .existsByBatteryManagementRecordIdAndQuestionnaireAbbreviationIn(
                batteryRecordId,
                java.util.List.of("ILA", "ILB")
            );

        if (hasIntralaboralRecords) {
            resultFormatter.throwBusinessRuleViolation(
                ErrorCode.PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED.getCode(),
                ErrorCode.PERSON_EVALUATED_DETAILS_DELETE_NOT_ALLOWED.getMessageKey()
            );
            return;
        }
        personEvaluatedDetailsCommandRepository.deleteById(personEvaluatedDetailsId);

        boolean hasAnyQuestionnaireRecords = questionnaireManagementRecordQueryRepository
            .existsByBatteryManagementRecordId(batteryRecordId);

        if (!hasAnyQuestionnaireRecords) {

            BatteryManagementRecord record = batteryManagementRecordQueryRepository
                .getBatteryManagementRecordById(batteryRecordId)
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(
                            ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                            "El registro de gestión de batería con ID " + batteryRecordId + " no fue encontrado."
                        )
                    );
                    return null;
                });

            BatteryManagementRecordStatus createdStatus = batteryManagementRecordStatusQueryRepository
                .getStatusByName("Creado")
                .orElseGet(() -> {
                    resultFormatter.throwEntityNotFound(
                        ErrorCode.ENTITY_NOT_FOUND.getCode(),
                        String.format(
                            ErrorCode.ENTITY_NOT_FOUND.getMessageKey(),
                            "El estado 'Creado' no fue encontrado."
                        )
                    );
                    return null;
                });

            record.setStatus(createdStatus);

            batteryManagementRecordCommandRepository
                .updateBatteryManagementRecord(record)
                .orElseGet(() -> {
                    resultFormatter.throwEntityCreationFailed(
                        ErrorCode.ENTITY_UPDATE_ERROR.getCode(),
                        String.format(
                            ErrorCode.ENTITY_UPDATE_ERROR.getMessageKey(),
                            "No se pudo actualizar el estado del registro de gestión de batería con ID " + batteryRecordId
                        )
                    );
                    return null;
                });
        }
    }

    /* ========================== Helpers de resolución ========================== */

    /**
     * Resuelve y valida un registro de gestión de batería.
     *
     * @param input el registro de gestión de batería a resolver
     * @return el registro resuelto
     */
    private BatteryManagementRecord resolveBatteryRecord(BatteryManagementRecord input) {
        Long id = safeId(input, input::getId, "registro de gestión de batería");
        return fetchOrThrow(
            () -> batteryManagementRecordQueryRepository.getBatteryManagementRecordById(id),
            String.format("El registro de gestión de batería con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un género.
     *
     * @param input el género a resolver
     * @return el género resuelto
     */
    private Gender resolveGender(Gender input) {
        Long id = safeId(input, input::getId, "género");
        return fetchOrThrow(
            () -> catalogQueryRepository.getGenderById(id),
            String.format("El género con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un estado civil.
     *
     * @param input el estado civil a resolver
     * @return el estado civil resuelto
     */
    private CivilStatus resolveCivilStatus(CivilStatus input) {
        Long id = safeId(input, input::getId, "estado civil");
        return fetchOrThrow(
            () -> catalogQueryRepository.getCivilStatusById(id),
            String.format("El estado civil con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un nivel de educación.
     *
     * @param input el nivel de educación a resolver
     * @return el nivel de educación resuelto
     */
    private EducationLevel resolveEducationLevel(EducationLevel input) {
        Long id = safeId(input, input::getId, "nivel de educación");
        return fetchOrThrow(
            () -> catalogQueryRepository.getEducationLevelById(id),
            String.format("El nivel de educación con ID %d no fue encontrado.", id)
        );
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
        return fetchOrThrow(
            () -> catalogQueryRepository.getCityById(id),
            String.format("La ciudad de %s con ID %d no fue encontrada.", contexto, id)
        );
    }

    /**
     * Resuelve y valida un nivel socioeconómico.
     *
     * @param input el nivel socioeconómico a resolver
     * @return el nivel socioeconómico resuelto
     */
    private SocioeconomicLevel resolveSocioeconomicLevel(SocioeconomicLevel input) {
        Long id = safeId(input, input::getId, "nivel socioeconómico");
        return fetchOrThrow(
            () -> catalogQueryRepository.getSocioeconomicLevelById(id),
            String.format("El nivel socioeconómico con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un tipo de vivienda.
     *
     * @param input el tipo de vivienda a resolver
     * @return el tipo de vivienda resuelto
     */
    private HousingType resolveHousingType(HousingType input) {
        Long id = safeId(input, input::getId, "tipo de vivienda");
        return fetchOrThrow(
            () -> catalogQueryRepository.getHousingTypeById(id),
            String.format("El tipo de vivienda con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un tipo de cargo.
     *
     * @param input el tipo de cargo a resolver
     * @return el tipo de cargo resuelto
     */
    private JobPositionType resolveJobPositionType(JobPositionType input) {
        Long id = safeId(input, input::getId, "tipo de cargo");
        return fetchOrThrow(
            () -> catalogQueryRepository.getJobPositionTypeById(id),
            String.format("El tipo de cargo con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un tipo de contrato.
     *
     * @param input el tipo de contrato a resolver
     * @return el tipo de contrato resuelto
     */
    private ContractType resolveContractType(ContractType input) {
        Long id = safeId(input, input::getId, "tipo de contrato");
        return fetchOrThrow(
            () -> catalogQueryRepository.getContractTypeById(id),
            String.format("El tipo de contrato con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Resuelve y valida un tipo de salario.
     *
     * @param input el tipo de salario a resolver
     * @return el tipo de salario resuelto
     */
    private SalaryType resolveSalaryType(SalaryType input) {
        Long id = safeId(input, input::getId, "tipo de salario");
        return fetchOrThrow(
            () -> catalogQueryRepository.getSalaryTypeById(id),
            String.format("El tipo de salario con ID %d no fue encontrado.", id)
        );
    }

    /**
     * Helper genérico para obtener un recurso o lanzar excepción estandarizada.
     *
     * @param fetcher      supplier que obtiene el Optional del recurso
     * @param notFoundMsg  mensaje específico cuando no existe
     * @return recurso resuelto
     */
    private <T> T fetchOrThrow(Supplier<Optional<T>> fetcher, String notFoundMsg) {
        return fetcher.get().orElseGet(() -> {
            resultFormatter.throwEntityNotFound(
                ErrorCode.ENTITY_NOT_FOUND.getCode(),
                String.format(ErrorCode.ENTITY_NOT_FOUND.getMessageKey(), notFoundMsg)
            );
            return null; // requerido por el compilador
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
                ErrorCode.BAD_REQUEST.getCode(),
                String.format("El %s es obligatorio y debe contener un ID.", nombre)
            );
            return null; // requerido por el compilador
        }

        return id;
    }

    /* ========================== Helpers de resolución ========================== */

    /**
     * Normaliza un texto: elimina espacios y convierte la primera letra en mayúscula.
     */
    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        value = value.trim();
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }
}
