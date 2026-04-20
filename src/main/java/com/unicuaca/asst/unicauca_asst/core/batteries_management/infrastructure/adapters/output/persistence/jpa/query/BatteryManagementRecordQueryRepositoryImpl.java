package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.query;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.BatteryManagementRecordStatus;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output.BatteryManagementRecordQueryRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.BatteryManagementRecordStatusSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.specifications.BatteryManagementRecordSpecification;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordPersistenceMapper;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers.BatteryManagementRecordStatusPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del puerto de salida {@link BatteryManagementRecordQueryRepository}
 * utilizando JPA como tecnología de persistencia.
 *
 * <p>Actúa como adaptador entre el dominio y la infraestructura para recuperar registros
 * de gestión de baterías, incluyendo consultas paginadas por estado, por término de búsqueda,
 * por prefijo de identificación, y búsquedas dinámicas con múltiples filtros mediante
 * {@link BatteryManagementRecordSpecification}.</p>
 */
@RequiredArgsConstructor
@Repository
public class BatteryManagementRecordQueryRepositoryImpl implements BatteryManagementRecordQueryRepository {

    private final BatteryManagementRecordSpringJpaRepository batteryManagementRecordSpringJpaRepository;
    private final BatteryManagementRecordPersistenceMapper batteryManagementRecordPersistenceMapper;
    private final BatteryManagementRecordStatusSpringJpaRepository batteryManagementRecordStatusSpringJpaRepository;
    private final BatteryManagementRecordStatusPersistenceMapper batteryManagementRecordStatusPersistenceMapper;

    /**
     * Busca un registro de gestión de batería por su identificador y lo transforma a su representación de dominio.
     * @param id identificador del registro de gestión de batería
     * @return un {@link Optional} con el registro encontrado o vacío si no existe
     */
    @Override
    public Optional<BatteryManagementRecord> getBatteryManagementRecordById(Long id) {
        return batteryManagementRecordSpringJpaRepository.findByIdWithRelations(id)
                .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Busca un estado de registro de gestión de batería por su nombre y lo transforma a su representación de dominio.
     * @param name nombre del estado del registro de gestión de batería
     * @return un {@link Optional} con el estado encontrado o vacío si no existe
     */
    @Override
    public Optional<BatteryManagementRecordStatus> getBatteryManagementRecordStatusByName(String name) {
        return batteryManagementRecordStatusSpringJpaRepository.findByName(name)
                .map(batteryManagementRecordStatusPersistenceMapper::toDomain);
    }

    /**
     * Verifica si existe un registro de gestión de batería con el ID proporcionado.
     * @param id identificador del registro de gestión de batería
     * @return true si el registro existe, false si no
     */
    @Override
    public boolean existsById(Long id) {
        return batteryManagementRecordSpringJpaRepository.existsById(id);
    }

    /**
     * Obtiene todos los registros de gestión de batería asociados a una persona evaluada específica.
     * @param personEvaluatedId ID de la persona evaluada
     * @return lista de registros de gestión de batería asociados a la persona evaluada
     */
    @Override
    public List<BatteryManagementRecord> getBatteryManagementRecordsByPersonEvaluatedId(Long personEvaluatedId) {
        List<BatteryManagementRecordEntity> entities = batteryManagementRecordSpringJpaRepository.findAllByPersonEvaluatedId(personEvaluatedId);

        return entities.stream()
                .map(batteryManagementRecordPersistenceMapper::toDomain)
                .toList();
    }

    /**
     * Verifica si existe un registro de gestión de batería asociado a una persona evaluada específica.
     * @param personEvaluatedId ID de la persona evaluada
     * @return true si existe un registro asociado, false en caso contrario
     */
    @Override
    public boolean existsByPersonEvaluatedId(Long personEvaluatedId) {
        return batteryManagementRecordSpringJpaRepository.existsByPersonEvaluatedId(personEvaluatedId);
    }

    /**
     * Verifica si una persona evaluada tiene al menos un registro de gestión de batería en alguno de los estados proporcionados.
     *
     * @param personEvaluatedId identificador de la persona evaluada
     * @param statusNames lista de nombres de estados a buscar
     * @return {@code true} si existe al menos un registro en alguno de los estados, {@code false} en caso contrario
     */
    @Override
    public boolean existsByPersonEvaluatedIdAndStatusNameIn(Long personEvaluatedId, List<String> statusNames) {
        return batteryManagementRecordSpringJpaRepository.existsByPersonEvaluatedIdAndStatusNameIn(personEvaluatedId, statusNames);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada por su nombre de estado.
     *
     * @param statusName nombre del estado a filtrar (ej: "Cerrado")
     * @param page número de página (0-indexed)
     * @param size tamaño de la página
     * @param sort criterios de ordenamiento
     * @return una página de registros de gestión de baterías con el estado especificado
     */
    @Override
    public Page<BatteryManagementRecord> listPagedByStatus(String statusName, Integer page, Integer size, Sort sort) {
        return batteryManagementRecordSpringJpaRepository
            .listPagedByStatus(statusName, PageRequest.of(page, size, sort))
            .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada por su nombre de estado y filtrando por término de búsqueda
     * en número de identificación o nombre del área de trabajo.
     *
     * @param statusName nombre del estado a filtrar (ej: "Cerrado")
     * @param searchTerm término de búsqueda para filtrar por número de identificación o nombre del área de trabajo (opcional)
     * @param page número de página (0-indexed)
     * @param size tamaño de la página
     * @param sort criterios de ordenamiento
     * @return una página de registros de gestión de baterías con el estado especificado y que coinciden con el término de búsqueda
     */
    @Override
    public Page<BatteryManagementRecord> listPagedByStatusWithSearchTerm(String statusName, String searchTerm, Integer page, Integer size, Sort sort) {
        return batteryManagementRecordSpringJpaRepository
            .listPagedByStatusWithSearchTerm(statusName, searchTerm, PageRequest.of(page, size, sort))
            .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Lista registros de gestión de baterías de forma paginada,
     * excluyendo los que tengan el estado indicado (ej: "Cerrado").
     *
     * @param excludedStatus nombre del estado a excluir (ej: "Cerrado")
     * @param page número de página (0-indexed)
     * @param size tamaño de la página
     * @param sort criterios de ordenamiento
     * @return página de registros de gestión de baterías excluyendo el estado especificado
     */
    @Override
    public Page<BatteryManagementRecord> listPagedExcludingStatus(String excludedStatus, Integer page, Integer size, Sort sort) {
        return batteryManagementRecordSpringJpaRepository
            .listPagedExcludingStatus(excludedStatus, PageRequest.of(page, size, sort))
            .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico
     * y filtrando por prefijo de número de identificación,
     * ordenados por el criterio proporcionado de manera descendente.
     *
     * @param excludedStatus nombre del estado a excluir
     * @param identificationNumberPrefix prefijo del número de identificación para filtrar
     * @param page número de página (0-indexed)
     * @param size tamaño de la página
     * @param sort criterios de ordenamiento
     * @return página de registros de gestión de baterías que coinciden con el prefijo y excluyen el estado especificado
     */
    @Override
    public Page<BatteryManagementRecord> listPaginatedByIdentificationPrefix(String excludedStatus, String identificationNumberPrefix, Integer page, Integer size, Sort sort) {
        return batteryManagementRecordSpringJpaRepository
            .listPagedByIdentificationPrefixExcludingStatus(excludedStatus, identificationNumberPrefix, PageRequest.of(page, size, sort))
            .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Lista registros de gestión de baterías paginados, excluyendo un estado específico
     * y filtrando por término de búsqueda en número de identificación o nombre del área de trabajo,
     * ordenados según el criterio proporcionado.
     *
     * @param excludedStatus nombre del estado a excluir
     * @param searchTerm término de búsqueda para filtrar por número de identificación
     * @param page número de página (0-indexed)
     * @param size tamaño de página
     * @param sort criterio de ordenamiento
     * @return una página de {@link BatteryManagementRecord}
     */
    @Override
    public Page<BatteryManagementRecord> listPagedExcludingStatusWithSearchTerm(String excludedStatus, String searchTerm, Integer page, Integer size, Sort sort) {
        return batteryManagementRecordSpringJpaRepository
            .listPagedExcludingStatusWithSearchTerm(excludedStatus, searchTerm, PageRequest.of(page, size, sort))
            .map(batteryManagementRecordPersistenceMapper::toDomain);
    }

    /**
     * Lista baterías cerradas paginadas con filtros opcionales múltiples usando JPA Criteria.
     *
     * @param identificationNumber número de identificación (prefijo, puede ser null)
     * @param workAreaName         área de trabajo (contenido parcial, puede ser null)
     * @param dateFrom             fecha inicial del rango (puede ser null)
     * @param dateTo               fecha final del rango (puede ser null)
     * @param identificationTypeId ID del tipo de identificación (puede ser null)
     * @param jobPositionTypeId    ID del tipo de cargo (puede ser null)
     * @param intralaboralForm     forma intralaboral: "A" (cargos 1-2) o "B" (cargos 3-4), puede ser null
     * @param page                 número de página
     * @param size                 tamaño de página
     * @return página de baterías cerradas filtradas
     */
    @Override
    public Page<BatteryManagementRecord> listClosedWithMultipleFilters(
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    ) {
        Specification<BatteryManagementRecordEntity> spec =
            BatteryManagementRecordSpecification.withClosedStatusAndFilters(
                identificationNumber, workAreaName, dateFrom, dateTo,
                identificationTypeId, jobPositionTypeId, intralaboralForm
            );
        Page<BatteryManagementRecordEntity> entityPage =
            batteryManagementRecordSpringJpaRepository.findAll(spec, PageRequest.of(page, size));
        List<BatteryManagementRecord> domainList = entityPage.getContent().stream()
            .map(batteryManagementRecordPersistenceMapper::toDomain)
            .toList();
        return new PageImpl<>(domainList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    /**
     * {@inheritDoc}
     *
     * <p>Usa {@link BatteryManagementRecordSpecification#withSpaceAndFilters} para construir
     * la Specification dinámica que restringe los resultados a las baterías asociadas al
     * espacio indicado.</p>
     */
    @Override
    public Page<BatteryManagementRecord> listByAnalysisSpaceWithMultipleFilters(
        Long spaceId,
        String identificationNumber, String workAreaName,
        LocalDateTime dateFrom, LocalDateTime dateTo,
        Long identificationTypeId, Long jobPositionTypeId,
        String intralaboralForm,
        Integer page, Integer size
    ) {
        Specification<BatteryManagementRecordEntity> spec =
            BatteryManagementRecordSpecification.withSpaceAndFilters(
                spaceId, identificationNumber, workAreaName, dateFrom, dateTo,
                identificationTypeId, jobPositionTypeId, intralaboralForm
            );
        Page<BatteryManagementRecordEntity> entityPage =
            batteryManagementRecordSpringJpaRepository.findAll(spec, PageRequest.of(page, size));
        List<BatteryManagementRecord> domainList = entityPage.getContent().stream()
            .map(batteryManagementRecordPersistenceMapper::toDomain)
            .toList();
        return new PageImpl<>(domainList, entityPage.getPageable(), entityPage.getTotalElements());
    }
}
