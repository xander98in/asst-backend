package com.unicuaca.asst.unicauca_asst.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuración central de OpenAPI (Swagger) para el proyecto ASST.
 *
 * Define la metadata global de la API (título, versión, contact, etc.)
 * y configura los distintos grupos de documentación usando {@link GroupedOpenApi},
 * de manera que los módulos (catálogos, personas, baterías, cuestionarios, etc.)
 * queden organizados y navegables en Swagger UI.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "ASST",
        version = "v1",
        description = "API para la gestión de evaluaciones psicológicas en la Universidad del Cauca",
        contact = @Contact(name = "Equipo ASST", email = "soporte@unicauca.edu.co"),
        license = @License(name = "Apache-2.0")
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local"),
        @Server(url = "https://api.unicauca.edu.co", description = "Prod")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT de acceso. Obtenerlo desde POST /auth/login"
)
@Configuration
public class OpenApiConfig {

    /**
     * Grupo de documentación para los catálogos comunes
     * (tipos de identificación, géneros, estados civiles, etc.).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/catalog/**</code>.
     *
     * @return configuración del grupo OpenAPI de catálogos
     */
    @Bean
    GroupedOpenApi catalogsGroup() {
        return GroupedOpenApi.builder()
            .group("catalogs")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/catalog/**")
            .build();
    }

    /**
     * Grupo de documentación para las personas evaluadas
     * y su información asociada.
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/person-evaluated/**</code>.
     *
     * @return configuración del grupo OpenAPI de personas evaluadas
     */
    @Bean
    GroupedOpenApi personasGroup() {
        return GroupedOpenApi.builder()
            .group("personas")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/person-evaluated/**")  // Si tienes diferentes rutas para personas
            .build();
    }

    /**
     * Grupo de documentación para los detalles de las personas evaluadas.
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/person-evaluated-details/**</code>.
     *
     * @return configuración del grupo OpenAPI de detalles de personas evaluadas
     */
    @Bean
    GroupedOpenApi personEvaluatedDetailsGroup() {
        return GroupedOpenApi.builder()
            .group("detalles-persona-evaluada")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/person-evaluated-details/**")
            .build();
    }

    /**
     * Grupo de documentación para los registros de gestión de baterías,
     * que representan los procesos/gestiones asociados a la aplicación de la batería
     * de instrumentos psicosociales.
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/battery-management-record/**</code>.
     *
     * @return configuración del grupo OpenAPI de registros de gestión de baterías
     */
    @Bean
    GroupedOpenApi BatteryManagementRecordGroup() {
        return GroupedOpenApi.builder()
            .group("registros-gestion-baterias")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/battery-management-record/**")
            .build();
    }

    /**
     * Grupo de documentación para los cuestionarios
     * (intralaboral, extralaboral, estrés, etc.).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/questionnaires/**</code>.
     *
     * @return configuración del grupo OpenAPI de cuestionarios
     */
    @Bean
    GroupedOpenApi QuestionnaireGroupedOpenApi() {
        return GroupedOpenApi.builder()
            .group("cuestionarios")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/questionnaires/**")
            .build();
    }

    /**
     * Grupo de documentación para los estados de los registros de gestión
     * de cuestionarios (Creado, En diligenciamiento, Diligenciado, Cerrado, etc.).
     *
     * Agrupa todos los endpoints bajo el prefijo
     * <code>/asst/questionnaire-management-record-statuses/**</code>.
     *
     * @return configuración del grupo OpenAPI de estados de registros de gestión de cuestionarios
     */
    @Bean
    GroupedOpenApi QuestionnaireManagementRecordStatusGroup() {
        return GroupedOpenApi.builder()
            .group("estados-registro-gestion-cuestionarios")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/questionnaire-management-record-statuses/**")
            .build();
    }

    /**
     * Grupo de documentación para las preguntas de los cuestionarios.
     *
     * Incluye tanto las consultas simples de preguntas como aquellas
     * que devuelven información del cuestionario asociado.
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/questions/**</code>.
     *
     * @return configuración del grupo OpenAPI de preguntas de cuestionarios
     */
    @Bean
    GroupedOpenApi questionGroup() {
        return GroupedOpenApi.builder()
            .group("preguntas-cuestionarios")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/questions/**")
            .build();
    }

    /**
     * Grupo de documentación para los registros de gestión de cuestionarios,
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/questionnaire-management-record/**</code>.
     *
     * @return configuración del grupo OpenAPI de registros de gestión de cuestionarios
     */
    @Bean
    GroupedOpenApi questionnaireManagementRecordGroup() {
        return GroupedOpenApi.builder()
            .group("registros-gestion-cuestionarios")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/questionnaire-management-record/**")
            .build();
    }

    /**
     * Grupo de documentación para las respuestas de los cuestionarios
     * (envío y consulta de respuestas asociadas a un registro de gestión).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/questionnaire-response/**</code>.
     *
     * @return configuración del grupo OpenAPI de respuestas de cuestionarios
     */
    @Bean
    GroupedOpenApi questionnaireResponseGroup() {
        return GroupedOpenApi.builder()
            .group("respuestas-cuestionarios")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/questionnaire-response/**")
            .build();
    }

    /**
     * Grupo de documentación para los endpoints de autenticación
     * (login, refresh, logout).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/auth/**</code>.
     *
     * @return configuración del grupo OpenAPI de autenticación
     */
    @Bean
    GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
            .group("autenticacion")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers")
            .pathsToMatch("/auth/**")
            .build();
    }

    /**
     * Grupo de documentación para la gestión de usuarios del sistema
     * (crear, actualizar, cambiar estado, eliminar, listar).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/users/**</code>.
     *
     * @return configuración del grupo OpenAPI de usuarios del sistema
     */
    @Bean
    GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder()
            .group("usuarios-sistema")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/users/**")
            .build();
    }

    /**
     * Grupo de documentación para el módulo de informes de riesgo psicosocial
     * (informes individuales, grupales, espacios de análisis).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/reports/**</code>.
     *
     * @return configuración del grupo OpenAPI del módulo de informes
     */
    @Bean
    GroupedOpenApi reportsGroup() {
        return GroupedOpenApi.builder()
            .group("informes")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.reports.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/reports/**")
            .build();
    }
}