package com.unicuaca.asst.unicauca_asst.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
@Configuration
public class OpenApiConfig {

    /**
     * Grupo de documentación para los catálogos comunes
     * (tipos de identificación, géneros, estados civiles, etc.).
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/catalog/**</code>.
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
     * Grupo de documentación para los registros de gestión de baterías,
     * que representan los procesos/gestiones asociados a la aplicación de la batería
     * de instrumentos psicosociales.
     *
     * Agrupa todos los endpoints bajo el prefijo <code>/asst/battery-management-record/**</code>.
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
     */
    @Bean
    GroupedOpenApi questionGroup() {
        return GroupedOpenApi.builder()
            .group("preguntas-cuestionarios")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/questions/**")
            .build();
    }

}
