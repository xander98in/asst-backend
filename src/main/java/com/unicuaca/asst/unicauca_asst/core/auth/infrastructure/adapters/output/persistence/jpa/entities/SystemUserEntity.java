package com.unicuaca.asst.unicauca_asst.core.auth.infrastructure.adapters.output.persistence.jpa.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.unicuaca.asst.unicauca_asst.common.infrastructure.adapters.output.persistence.jpa.entities.AuditableEntity;
import com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities.PersonEvaluatedEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un usuario del sistema.
 *
 * <p>Mapea la tabla {@code usuarios_sistema} en la base de datos y hereda los campos
 * de auditoría ({@code fecha_creacion}, {@code fecha_actualizacion}) de {@link AuditableEntity}.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios_sistema")
public class SystemUserEntity extends AuditableEntity {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Correo electrónico del usuario.
     */
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Nombre de usuario único en el sistema.
     */
    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    private String username;

    /**
     * Nombre completo del usuario.
     */
    @Column(name = "nombre_completo", nullable = false, length = 200)
    private String fullName;

    /**
     * Fecha y hora en que el usuario se registró.
     */
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime registeredAt;

    /**
     * Estado actual del usuario. ON DELETE RESTRICT.
     */
    @ManyToOne
    @JoinColumn(name = "estado_usuario_id", nullable = false, foreignKey = @ForeignKey(
        name = "fk_usuario_estado",
        foreignKeyDefinition = "FOREIGN KEY (estado_usuario_id) REFERENCES estados_usuario(id) ON DELETE RESTRICT"
    ))
    private UserStatusEntity status;

    /**
     * Roles asignados al usuario. Tabla intermedia "usuarios_sistema_roles".
     * ON DELETE CASCADE en usuario, ON DELETE RESTRICT en rol.
     */
    @ManyToMany
    @JoinTable(
        name = "usuarios_sistema_roles",
        joinColumns = @JoinColumn(
            name = "usuario_sistema_id",
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (usuario_sistema_id) REFERENCES usuarios_sistema(id) ON DELETE CASCADE")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "rol_id",
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE RESTRICT")
        )
    )
    private Set<RoleEntity> roles;

    /**
     * Persona evaluada vinculada al usuario. Puede ser nula.
     * Carga diferida (LAZY). ON DELETE CASCADE.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_evaluada_id", foreignKey = @ForeignKey(
        name = "fk_usuario_persona_evaluada",
        foreignKeyDefinition = "FOREIGN KEY (persona_evaluada_id) REFERENCES personas_evaluadas(id) ON DELETE CASCADE"
    ))
    private PersonEvaluatedEntity personEvaluated;
}
