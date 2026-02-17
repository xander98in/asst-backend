package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Questionnaire;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de consulta sobre el agregado {@link Questionnaire}.
 *
 * Define las firmas de los métodos que deben implementar los adaptadores de infraestructura
 * encargados de recuperar información desde fuentes externas, como bases de datos.
 */
public interface QuestionnaireQueryRepository {

    /**
     * Recupera todos los cuestionarios registrados en el sistema.
     *
     * @return una lista con todos los cuestionarios disponibles.
     */
    List<Questionnaire> getAll();

    /**
     * Obtiene un cuestionario a partir de su abreviatura exacta.
     *
     * @param abbreviation abreviatura del cuestionario (por ejemplo, "ILA", "EXT", "EST").
     * @return un {@link Optional} que contiene el cuestionario encontrado, o vacío si no existe.
     */
    Optional<Questionnaire> getByAbbreviation(String abbreviation);

    /**
     * Verifica si existe un cuestionario con la abreviatura especificada.
     *
     * @param abbreviation abreviatura del cuestionario a verificar.
     * @return {@code true} si existe un cuestionario con esa abreviatura, {@code false} en caso contrario.
     */
    boolean existsByAbbreviation(String abbreviation);

    /**
     * Verifica si existe un cuestionario con el identificador especificado.
     *
     * @param id Identificador del cuestionario a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Obtiene un cuestionario a partir de su nombre exacto.
     *
     * @param name nombre completo del cuestionario (por ejemplo, "Cuestionario Intralaboral - Forma A").
     * @return un {@link Optional} que contiene el cuestionario encontrado, o vacío si no existe.
     */
    Optional<Questionnaire> getByName(String name);

    /**
     * Verifica si existe un cuestionario con el nombre especificado.
     *
     * @param name nombre del cuestionario a verificar.
     * @return {@code true} si existe un cuestionario con ese nombre, {@code false} en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Obtiene un cuestionario a partir de su identificador único.
     *
     * @param id Identificador del cuestionario.
     * @return un {@link Optional} que contiene el cuestionario encontrado, o vacío si no existe.
     */
    Optional<Questionnaire> getById(Long id);
}
