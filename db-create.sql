-- ==========================================================
-- db-create.sql — Esquema completo del sistema ASST
-- Generado a partir de las entidades JPA del proyecto.
-- Ejecutar en MySQL antes de db-seed.sql.
-- Orden: catálogos comunes → baterías → auth
-- (tablas padre antes que tablas hija para respetar FK)
-- ==========================================================


-- ==========================================================
-- 1. CATÁLOGOS COMUNES
-- ==========================================================

CREATE TABLE IF NOT EXISTS tipos_identificacion (
    id_tipo_identificacion BIGINT NOT NULL AUTO_INCREMENT,
    nombre                 VARCHAR(120) NOT NULL,
    abreviatura            VARCHAR(10) NOT NULL,
    PRIMARY KEY (id_tipo_identificacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS estado_civil (
    id_estado_civil BIGINT NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(60) NOT NULL,
    PRIMARY KEY (id_estado_civil)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS nivel_estudio (
    id_nivel_estudio BIGINT NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(60) NOT NULL,
    PRIMARY KEY (id_nivel_estudio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tipo_vivienda (
    id_tipo_vivienda BIGINT NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_tipo_vivienda)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS estrato (
    id_estrato BIGINT NOT NULL AUTO_INCREMENT,
    nombre     VARCHAR(15) NOT NULL,
    PRIMARY KEY (id_estrato)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tipo_cargo (
    id_tipo_cargo BIGINT NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(120) NOT NULL,
    PRIMARY KEY (id_tipo_cargo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tipo_contrato (
    id_tipo_contrato BIGINT NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(60) NOT NULL,
    PRIMARY KEY (id_tipo_contrato)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS tipo_salario (
    id_tipo_salario BIGINT NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(120) NOT NULL,
    PRIMARY KEY (id_tipo_salario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sexo (
    id_sexo BIGINT NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_sexo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS departamentos (
    id     BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(10)  NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_departamento_codigo UNIQUE (codigo),
    CONSTRAINT uk_departamento_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ciudades (
    id              BIGINT NOT NULL AUTO_INCREMENT,
    codigo          VARCHAR(10)  NOT NULL,
    nombre          VARCHAR(150) NOT NULL,
    departamento_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ciudad_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ==========================================================
-- 2. MÓDULO BATERÍAS — Tablas de estado y catálogos
-- ==========================================================

CREATE TABLE IF NOT EXISTS estados_persona_evaluada (
    id_estado_persona_evaluada BIGINT NOT NULL AUTO_INCREMENT,
    nombre                     VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_estado_persona_evaluada),
    CONSTRAINT uk_estado_persona_evaluada_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS estados_registro_gestion_baterias (
    id_estado_registro_gestion_bateria BIGINT NOT NULL AUTO_INCREMENT,
    nombre                             VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_estado_registro_gestion_bateria),
    CONSTRAINT uk_estado_reg_bat_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS estados_registro_gestion_cuestionarios (
    id     BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_estado_reg_cuest_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cuestionarios (
    id           BIGINT NOT NULL AUTO_INCREMENT,
    nombre       VARCHAR(250)  NOT NULL,
    abreviatura  VARCHAR(10)   NOT NULL,
    descripcion  VARCHAR(1000),
    PRIMARY KEY (id),
    CONSTRAINT uk_cuestionario_nombre      UNIQUE (nombre),
    CONSTRAINT uk_cuestionario_abreviatura UNIQUE (abreviatura)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS preguntas (
    id              BIGINT NOT NULL AUTO_INCREMENT,
    texto           VARCHAR(1000) NOT NULL,
    orden           INT           NOT NULL,
    id_cuestionario BIGINT        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pregunta_cuestionario FOREIGN KEY (id_cuestionario) REFERENCES cuestionarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- NOTA: La entidad JPA define el nombre "opciones_respuesta".
-- El archivo db-seed.sql original usa "opciones_respuetas" (typo).
-- Si se ejecuta este script, verificar que db-seed.sql apunte al nombre correcto.
CREATE TABLE IF NOT EXISTS opciones_respuesta (
    id    BIGINT NOT NULL AUTO_INCREMENT,
    texto VARCHAR(100) NOT NULL,
    valor INT          NOT NULL,
    orden INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_opcion_respuesta_valor UNIQUE (valor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ==========================================================
-- 3. MÓDULO BATERÍAS — Tablas de negocio
-- ==========================================================

CREATE TABLE IF NOT EXISTS personas_evaluadas (
    id                       BIGINT NOT NULL AUTO_INCREMENT,
    tipo_identificacion_id   BIGINT       NOT NULL,
    numero_identificacion    VARCHAR(30)  NOT NULL,
    nombres                  VARCHAR(120) NOT NULL,
    apellidos                VARCHAR(120) NOT NULL,
    anio_nacimiento          INT          NOT NULL,
    correo_electronico       VARCHAR(100) NOT NULL,
    estado_persona_evaluada_id BIGINT     NOT NULL,
    fecha_creacion           DATETIME     NOT NULL,
    fecha_actualizacion      DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT uk_persona_tipo_numero        UNIQUE (tipo_identificacion_id, numero_identificacion),
    CONSTRAINT uk_persona_numero_ident       UNIQUE (numero_identificacion),
    CONSTRAINT uk_persona_correo             UNIQUE (correo_electronico),
    CONSTRAINT fk_persona_tipo_ident         FOREIGN KEY (tipo_identificacion_id)   REFERENCES tipos_identificacion (id_tipo_identificacion),
    CONSTRAINT fk_persona_estado             FOREIGN KEY (estado_persona_evaluada_id) REFERENCES estados_persona_evaluada (id_estado_persona_evaluada)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS registros_gestion_baterias (
    id_registro_gestion_bateria        BIGINT NOT NULL AUTO_INCREMENT,
    id_persona_evaluada                BIGINT   NOT NULL,
    id_estado_registro_gestion_bateria BIGINT   NOT NULL,
    fecha_creacion                     DATETIME NOT NULL,
    fecha_actualizacion                DATETIME,
    PRIMARY KEY (id_registro_gestion_bateria),
    CONSTRAINT fk_reg_bat_persona FOREIGN KEY (id_persona_evaluada)                REFERENCES personas_evaluadas (id),
    CONSTRAINT fk_reg_bat_estado  FOREIGN KEY (id_estado_registro_gestion_bateria) REFERENCES estados_registro_gestion_baterias (id_estado_registro_gestion_bateria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS personas_evaluadas_detalles (
    id                          BIGINT NOT NULL AUTO_INCREMENT,
    id_registro_gestion_bateria BIGINT   NOT NULL,
    id_sexo                     BIGINT,
    id_estado_civil             BIGINT,
    id_nivel_estudio            BIGINT,
    profesion                   VARCHAR(250),
    id_ciudad_residencia        BIGINT,
    id_estrato                  BIGINT,
    id_tipo_vivienda            BIGINT,
    numero_dependientes         INT,
    id_ciudad_trabajo           BIGINT,
    anios_en_empresa            VARCHAR(12),
    cargo                       VARCHAR(150),
    id_tipo_cargo               BIGINT,
    anios_en_cargo              VARCHAR(12),
    area_trabajo                VARCHAR(250),
    id_tipo_contrato            BIGINT,
    horas_diarias_trabajo       INT,
    id_tipo_salario             BIGINT,
    fecha_creacion              DATETIME NOT NULL,
    fecha_actualizacion         DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT uk_personas_evaluadas_detalles_registro_gestion_bateria UNIQUE (id_registro_gestion_bateria),
    CONSTRAINT fk_det_reg_bat      FOREIGN KEY (id_registro_gestion_bateria) REFERENCES registros_gestion_baterias (id_registro_gestion_bateria),
    CONSTRAINT fk_det_sexo         FOREIGN KEY (id_sexo)                    REFERENCES sexo (id_sexo),
    CONSTRAINT fk_det_estado_civil FOREIGN KEY (id_estado_civil)            REFERENCES estado_civil (id_estado_civil),
    CONSTRAINT fk_det_nivel_est    FOREIGN KEY (id_nivel_estudio)           REFERENCES nivel_estudio (id_nivel_estudio),
    CONSTRAINT fk_det_ciudad_res   FOREIGN KEY (id_ciudad_residencia)       REFERENCES ciudades (id),
    CONSTRAINT fk_det_estrato      FOREIGN KEY (id_estrato)                 REFERENCES estrato (id_estrato),
    CONSTRAINT fk_det_tipo_viv     FOREIGN KEY (id_tipo_vivienda)           REFERENCES tipo_vivienda (id_tipo_vivienda),
    CONSTRAINT fk_det_ciudad_trab  FOREIGN KEY (id_ciudad_trabajo)          REFERENCES ciudades (id),
    CONSTRAINT fk_det_tipo_cargo   FOREIGN KEY (id_tipo_cargo)              REFERENCES tipo_cargo (id_tipo_cargo),
    CONSTRAINT fk_det_tipo_contrat FOREIGN KEY (id_tipo_contrato)           REFERENCES tipo_contrato (id_tipo_contrato),
    CONSTRAINT fk_det_tipo_salario FOREIGN KEY (id_tipo_salario)            REFERENCES tipo_salario (id_tipo_salario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS registros_gestion_cuestionarios (
    id                                       BIGINT NOT NULL AUTO_INCREMENT,
    id_registro_gestion_bateria              BIGINT   NOT NULL,
    id_cuestionario                          BIGINT   NOT NULL,
    id_estado_registro_gestion_cuestionario  BIGINT   NOT NULL,
    fecha_creacion                           DATETIME NOT NULL,
    fecha_actualizacion                      DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_reg_cuest_bat    FOREIGN KEY (id_registro_gestion_bateria)             REFERENCES registros_gestion_baterias (id_registro_gestion_bateria),
    CONSTRAINT fk_reg_cuest_cuest  FOREIGN KEY (id_cuestionario)                         REFERENCES cuestionarios (id),
    CONSTRAINT fk_reg_cuest_estado FOREIGN KEY (id_estado_registro_gestion_cuestionario) REFERENCES estados_registro_gestion_cuestionarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS respuestas_cuestionarios (
    id                                BIGINT NOT NULL AUTO_INCREMENT,
    id_registro_gestion_cuestionario  BIGINT   NOT NULL,
    id_pregunta                       BIGINT   NOT NULL,
    id_opcion_respuesta               BIGINT   NOT NULL,
    fecha_creacion                    DATETIME NOT NULL,
    fecha_actualizacion               DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT uk_respuesta_registro_pregunta UNIQUE (id_registro_gestion_cuestionario, id_pregunta),
    CONSTRAINT fk_resp_reg_cuest FOREIGN KEY (id_registro_gestion_cuestionario) REFERENCES registros_gestion_cuestionarios (id),
    CONSTRAINT fk_resp_pregunta  FOREIGN KEY (id_pregunta)                      REFERENCES preguntas (id),
    CONSTRAINT fk_resp_opcion    FOREIGN KEY (id_opcion_respuesta)              REFERENCES opciones_respuesta (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ==========================================================
-- 4. MÓDULO AUTH
-- ==========================================================

CREATE TABLE IF NOT EXISTS estados_usuario (
    id     BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_estado_usuario_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS roles (
    id           BIGINT NOT NULL AUTO_INCREMENT,
    nombre       VARCHAR(100) NOT NULL,
    nombre_clave VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_rol_nombre_clave UNIQUE (nombre_clave)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios_sistema (
    id                   BIGINT NOT NULL AUTO_INCREMENT,
    correo_electronico   VARCHAR(150) NOT NULL,
    nombre_usuario       VARCHAR(100) NOT NULL,
    nombre_completo      VARCHAR(200) NOT NULL,
    fecha_registro       DATETIME     NOT NULL,
    estado_usuario_id    BIGINT       NOT NULL,
    persona_evaluada_id  BIGINT,
    fecha_creacion       DATETIME     NOT NULL,
    fecha_actualizacion  DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuario_correo   UNIQUE (correo_electronico),
    CONSTRAINT uk_usuario_username UNIQUE (nombre_usuario),
    CONSTRAINT fk_usuario_estado            FOREIGN KEY (estado_usuario_id)   REFERENCES estados_usuario (id) ON DELETE RESTRICT,
    CONSTRAINT fk_usuario_persona_evaluada  FOREIGN KEY (persona_evaluada_id) REFERENCES personas_evaluadas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios_sistema_roles (
    usuario_sistema_id BIGINT NOT NULL,
    rol_id             BIGINT NOT NULL,
    PRIMARY KEY (usuario_sistema_id, rol_id),
    CONSTRAINT fk_usr_rol_usuario FOREIGN KEY (usuario_sistema_id) REFERENCES usuarios_sistema (id) ON DELETE CASCADE,
    CONSTRAINT fk_usr_rol_rol     FOREIGN KEY (rol_id)             REFERENCES roles (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
