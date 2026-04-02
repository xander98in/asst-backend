package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.config.SecurityAccessDeniedHandler;
import com.unicuaca.asst.unicauca_asst.common.infrastructure.config.SecurityAuthenticationEntryPoint;
import com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.config.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Reglas de seguridad del módulo de gestión de baterías.
 *
 * <p>Define el {@link SecurityFilterChain} con {@code @Order(4)} para las rutas del módulo
 * de baterías: personas evaluadas, detalles sociodemográficos, registros de gestión de
 * baterías, cuestionarios, preguntas, estados y respuestas.</p>
 *
 * <p>Todas las operaciones requieren rol {@code ADMIN} o {@code PROFESIONAL_ASST}.</p>
 */
@Configuration
@RequiredArgsConstructor
public class BatteriesSecurityRules {

    private final JwtAuthFilter jwtAuthFilter;
    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * SecurityFilterChain para el módulo de gestión de baterías.
     *
     * <p>Rutas cubiertas:</p>
     * <ul>
     *   <li>{@code /asst/person-evaluated/**}</li>
     *   <li>{@code /asst/person-evaluated-details/**}</li>
     *   <li>{@code /asst/battery-management-record/**}</li>
     *   <li>{@code /asst/questionnaires/**}</li>
     *   <li>{@code /asst/questions/**}</li>
     *   <li>{@code /asst/questionnaire-management-record-statuses/**}</li>
     *   <li>{@code /asst/questionnaire-management-record/**}</li>
     *   <li>{@code /asst/questionnaire-response/**}</li>
     * </ul>
     *
     * @param http configuración de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    @Order(4)
    public SecurityFilterChain batteriesSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher(
                "/asst/person-evaluated/**",
                "/asst/person-evaluated-details/**",
                "/asst/battery-management-record/**",
                "/asst/questionnaires/**",
                "/asst/questions/**",
                "/asst/questionnaire-management-record-statuses/**",
                "/asst/questionnaire-management-record/**",
                "/asst/questionnaire-response/**"
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(formLogin -> formLogin.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeHttpRequests(auth -> auth
                // Persona Evaluada
                .requestMatchers(HttpMethod.POST, "/asst/person-evaluated")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.PUT, "/asst/person-evaluated/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.DELETE, "/asst/person-evaluated/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/person-evaluated/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                // Detalles Sociodemográficos
                .requestMatchers(HttpMethod.POST, "/asst/person-evaluated-details/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.PUT, "/asst/person-evaluated-details/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.DELETE, "/asst/person-evaluated-details/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/person-evaluated-details/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                // Registro de Gestión de Baterías
                .requestMatchers(HttpMethod.POST, "/asst/battery-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.DELETE, "/asst/battery-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.PATCH, "/asst/battery-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/battery-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                // Cuestionarios y Preguntas
                .requestMatchers(HttpMethod.GET, "/asst/questionnaires/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/questions/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/questionnaire-management-record-statuses/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                // Gestión de Cuestionarios
                .requestMatchers(HttpMethod.POST, "/asst/questionnaire-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.DELETE, "/asst/questionnaire-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/questionnaire-management-record/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                // Respuestas de Cuestionarios
                .requestMatchers(HttpMethod.POST, "/asst/questionnaire-response/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.PUT, "/asst/questionnaire-response/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")
                .requestMatchers(HttpMethod.GET, "/asst/questionnaire-response/**")
                    .hasAnyRole("ADMIN", "PROFESIONAL_ASST")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
