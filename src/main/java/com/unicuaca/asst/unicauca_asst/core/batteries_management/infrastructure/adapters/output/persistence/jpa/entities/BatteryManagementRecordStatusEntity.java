package com.unicuaca.asst.unicauca_asst.core.batteries_management.infrastructure.adapters.output.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa los estados de un registro de gestión de baterías.
 * Mapea la tabla "estados_registro_gestion_baterias".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estados_registro_gestion_baterias")
public class BatteryManagementRecordStatusEntity {

    /**
     * Identificador único del estado del registro de gestión de baterías.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_registro_gestion_bateria")
    @Id
    private Long id;

    /**
     * Nombre del estado del registro de gestión de baterías.
     */
    @Column(name = "nombre", nullable = false, length = 30, unique = true)
    private String name;
}
