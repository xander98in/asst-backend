package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.mappers;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.QuestionnaireManagementRecord;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.QuestionnaireManagementRecordEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre la entidad de persistencia de registro de gestión de cuestionario y el dominio.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        BatteryManagementRecordPersistenceMapper.class,
        QuestionnairePersistenceMapper.class,
        QuestionnaireManagementRecordStatusPersistenceMapper.class
    },
    builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface QuestionnaireManagementRecordPersistenceMapper {

    /**
     * Mapea una entidad de persistencia de registro de gestión de cuestionario a su dominio correspondiente.
     *
     * @param entity La entidad de persistencia del registro de gestión de cuestionario
     * @return El dominio correspondiente
     */
    @Mapping(target = "batteryManagementRecord", source = "batteryManagementRecord")
    @Mapping(target = "questionnaire", source = "questionnaire")
    @Mapping(target = "status", source = "status")
    QuestionnaireManagementRecord toDomain(QuestionnaireManagementRecordEntity entity);

    /**
     * Convierte el dominio a entidad.
     *
     * <p>Al usar {@link InheritInverseConfiguration}, MapStruct intentará usar los mappers
     * definidos en 'uses' para convertir las relaciones completas (Domain -&gt; Entity)
     * y no solo los IDs.</p>
     *
     * @param domain el modelo de dominio del registro de gestión de cuestionario
     * @return la entidad de persistencia correspondiente
     */
    @InheritInverseConfiguration
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    QuestionnaireManagementRecordEntity toEntity(QuestionnaireManagementRecord domain);
}
