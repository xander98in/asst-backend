package com.unicuaca.asst.unicauca_asst.common.infrastructure.config;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Filtro global de logging HTTP que registra cada petición entrante y su respuesta.
 *
 * <p>Intercepta todas las solicitudes HTTP (excepto Swagger y favicon) y genera
 * dos líneas de log por petición:</p>
 * <ul>
 *   <li><b>Entrada:</b> método HTTP, URI y dirección IP del cliente.</li>
 *   <li><b>Salida:</b> método HTTP, URI, código de estado y tiempo de respuesta en milisegundos.</li>
 * </ul>
 *
 * <p>El nivel de log de la respuesta varía según el código de estado:</p>
 * <ul>
 *   <li>{@code INFO} para respuestas exitosas (2xx/3xx).</li>
 *   <li>{@code WARN} para errores del cliente (4xx).</li>
 *   <li>{@code ERROR} para errores del servidor (5xx).</li>
 * </ul>
 *
 * <p>Se ejecuta con la mayor prioridad ({@link Ordered#HIGHEST_PRECEDENCE}) para
 * garantizar que el tiempo de respuesta medido incluya todo el procesamiento de la cadena de filtros.</p>
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    /**
     * Registra la petición entrante, delega al siguiente filtro de la cadena y
     * registra la respuesta con el tiempo de ejecución.
     *
     * @param request     solicitud HTTP entrante
     * @param response    respuesta HTTP
     * @param filterChain cadena de filtros de Spring
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException      si ocurre un error de E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        log.info("--> {} {} [IP: {}]", method, uri, ip);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();

            if (status >= 500) {
                log.error("<-- {} {} [{}] ({}ms)", method, uri, status, duration);
            } else if (status >= 400) {
                log.warn("<-- {} {} [{}] ({}ms)", method, uri, status, duration);
            } else {
                log.info("<-- {} {} [{}] ({}ms)", method, uri, status, duration);
            }
        }
    }

    /**
     * Excluye del logging las rutas de documentación (Swagger, OpenAPI) y favicon.
     *
     * @param request solicitud HTTP a evaluar
     * @return {@code true} si la ruta debe ser excluida del logging
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/favicon.ico");
    }
}
