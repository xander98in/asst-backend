package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un estado de usuario del sistema.
 *
 * <p>Mapea la tabla {@code estados_usuario} en la base de datos y contiene los valores
 * del catálogo de estados posibles (Activo, Inactivo, Bloqueado).</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estados_usuario")
public class UserStatusEntity {

    /**
     * Identificador único del estado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del estado.
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String name;
}
