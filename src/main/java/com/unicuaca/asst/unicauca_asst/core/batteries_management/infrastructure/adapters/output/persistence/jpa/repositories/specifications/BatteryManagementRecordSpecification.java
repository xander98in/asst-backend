package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.repositories.specifications;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.BatteryManagementRecordEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedDetailsEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

/**
 * Constructor de especificaciones JPA para filtrado dinámico de registros de gestión
 * de baterías con estado "Cerrado".
 *
 * <p>Permite combinar múltiples criterios opcionales en una misma consulta sin necesidad
 * de generar JPQL estático por cada combinación posible.</p>
 */
@Component
public class BatteryManagementRecordSpecification {

    /**
     * Construye una Specification dinámica para filtrar baterías cerradas
     * con múltiples criterios opcionales.
     *
     * @param identificationNumber número de identificación (filtro por prefijo, puede ser null)
     * @param workAreaName         nombre del área de trabajo (filtro por contenido, puede ser null)
     * @param dateFrom             fecha inicial del rango (puede ser null, si dateTo también es null no filtra)
     * @param dateTo               fecha final del rango (puede ser null, si dateFrom también es null no filtra)
     * @param identificationTypeId ID del tipo de identificación (puede ser null)
     * @param jobPositionTypeId    ID del tipo de cargo (puede ser null)
     * @param intralaboralForm     forma intralaboral: "A" (cargos 1-2) o "B" (cargos 3-4), puede ser null
     * @return Specification con los filtros aplicados
     */
    public static Specification<BatteryManagementRecordEntity> withClosedStatusAndFilters(
        String identificationNumber,
        String workAreaName,
        LocalDateTime dateFrom,
        LocalDateTime dateTo,
        Long identificationTypeId,
        Long jobPositionTypeId,
        String intralaboralForm
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<BatteryManagementRecordEntity, PersonEvaluatedEntity> personJoin =
                root.join("personEvaluated", JoinType.INNER);

            Join<BatteryManagementRecordEntity, ?> statusJoin =
                root.join("status", JoinType.INNER);

            predicates.add(cb.equal(statusJoin.get("name"), "Cerrado"));

            if (identificationNumber != null && !identificationNumber.isBlank()) {
                predicates.add(cb.like(
                    personJoin.get("identificationNumber"),
                    identificationNumber.trim() + "%"
                ));
            }

            if (identificationTypeId != null) {
                predicates.add(cb.equal(
                    personJoin.get("identificationType").get("id"),
                    identificationTypeId
                ));
            }

            boolean hasWorkArea = workAreaName != null && !workAreaName.isBlank();
            boolean hasJobPosition = jobPositionTypeId != null;
            boolean hasIntralaboralForm = intralaboralForm != null && !intralaboralForm.isBlank();

            if (hasWorkArea || hasJobPosition || hasIntralaboralForm) {
                Subquery<Long> detailsSubquery = query.subquery(Long.class);
                Root<PersonEvaluatedDetailsEntity> detailsRoot = detailsSubquery.from(PersonEvaluatedDetailsEntity.class);
                detailsSubquery.select(cb.literal(1L));

                List<Predicate> detailsPredicates = new ArrayList<>();
                detailsPredicates.add(cb.equal(
                    detailsRoot.get("batteryManagementRecord").get("id"),
                    root.get("id")
                ));

                if (hasWorkArea) {
                    detailsPredicates.add(cb.like(
                        cb.lower(detailsRoot.get("workAreaName")),
                        "%" + workAreaName.trim().toLowerCase() + "%"
                    ));
                }

                if (hasJobPosition) {
                    detailsPredicates.add(cb.equal(
                        detailsRoot.get("jobPositionType").get("id"),
                        jobPositionTypeId
                    ));
                }

                if (hasIntralaboralForm) {
                    if ("A".equalsIgnoreCase(intralaboralForm)) {
                        detailsPredicates.add(detailsRoot.get("jobPositionType").get("id").in(1L, 2L));
                    } else if ("B".equalsIgnoreCase(intralaboralForm)) {
                        detailsPredicates.add(detailsRoot.get("jobPositionType").get("id").in(3L, 4L));
                    }
                }

                detailsSubquery.where(cb.and(detailsPredicates.toArray(new Predicate[0])));
                predicates.add(cb.exists(detailsSubquery));
            }

            if (dateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom));
            }
            if (dateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), dateTo));
            }

            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
