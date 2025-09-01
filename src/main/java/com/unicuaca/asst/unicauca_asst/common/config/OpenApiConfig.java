package com.unicuaca.asst.unicauca_asst.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "ASST – Catálogos",
        version = "v1",
        description = "API para catálogos comunes (tipos de identificación, etc.)",
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

    @Bean
    public GroupedOpenApi catalogsGroup() {
        return GroupedOpenApi.builder()
            .group("catalogs")
            .packagesToScan("com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.input.controllers")
            .pathsToMatch("/asst/catalog/**")
            .build();
    }

}
