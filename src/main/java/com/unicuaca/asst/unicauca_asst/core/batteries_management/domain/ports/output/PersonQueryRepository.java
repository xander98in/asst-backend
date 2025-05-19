package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.output;

import java.util.Optional;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;

/**
 * Puerto de salida que define las operaciones de consulta relacionadas con el agregado {@link Person}.
 *
 * Esta interfaz representa un contrato que debe implementar la infraestructura para consultar
 * información de personas desde una fuente de datos (por ejemplo, una base de datos relacional).
 *
 * <p>Hace parte del patrón de arquitectura hexagonal, permitiendo que la lógica de dominio
 * permanezca desacoplada de los detalles técnicos de persistencia.</p>
 */
public interface PersonQueryRepository {

    /**
     * Consulta una persona a partir de su identificador único.
     *
     * @param id identificador de la persona
     * @return un {@link Optional} con la persona encontrada o vacío si no existe
     */
    Optional<Person> getPersonById(Long id);

    /**
     * Verifica si existe una persona con el ID proporcionado.
     *
     * @param id identificador de la persona
     * @return true si la persona existe, false si no
     */
    boolean existsById(Long id);

    /**
     * Verifica si existe una persona con un tipo y número de identificación específicos.
     *
     * @param identificationTypeId ID del tipo de identificación (por ejemplo: cédula, pasaporte)
     * @param identificationNumber número de identificación de la persona
     * @return true si la persona existe, false si no
     */
    boolean existsByIdentification(Long identificationTypeId, String identificationNumber);
}
