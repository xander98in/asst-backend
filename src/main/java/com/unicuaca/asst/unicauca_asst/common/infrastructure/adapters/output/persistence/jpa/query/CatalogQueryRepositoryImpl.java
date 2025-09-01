package com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicuaca.asst.unicauca_asst.common.domain.models.IdentificationType;
import com.unicuaca.asst.unicauca_asst.common.domain.ports.output.CatalogQueryRepository;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.IdentificationTypeEntity;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.repositories.IdentificationTypeSpringJpaRepository;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.mappers.CatalogPersistenceMapper;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del puerto de salida para las consultas de catálogos.
 *
 * Se encarga de recuperar la información desde la base de datos utilizando JPA.
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CatalogQueryRepositoryImpl implements CatalogQueryRepository {

    private final IdentificationTypeSpringJpaRepository identificationTypeSpringJpaRepository;
    private final CatalogPersistenceMapper catalogPersistenceMapper;

    /**
     * Obtiene una lista de todos los tipos de identificación.
     *
     * @return una lista de objetos IdentificationType
     */
    @Override
    public List<IdentificationType> getIdTypes() {
        List<IdentificationTypeEntity> entities = identificationTypeSpringJpaRepository.findAllByOrderByDescriptionAsc();
        return entities.stream()
                .map(catalogPersistenceMapper::toIdentificationTypeDomain)
                .toList();
    }

}
