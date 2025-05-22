package com.unicuaca.asst.unicauca_asst.common.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Configuración centralizada de los archivos de mensajes (.properties) usados para validación,
 * mensajes multilingües y errores personalizados en toda la aplicación.
 */
@Configuration
public class MessageSourceConfig {

    /**
     * Bean principal para la carga de archivos de mensajes.
     * Spring usará esta instancia para resolver mensajes por clave e idioma.
     *
     * @return MessageSource con los basenames definidos
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(
            "classpath:i18n/validation-messages",
            "classpath:i18n/error-messages"
        );
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setUseCodeAsDefaultMessage(true); // Si no encuentra la clave, muestra la clave como mensaje
        return messageSource;
    }
}
