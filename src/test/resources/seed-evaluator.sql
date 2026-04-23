MERGE INTO estados_usuario (id, nombre) VALUES (1, 'Activo');
MERGE INTO roles (id, nombre, nombre_clave) VALUES (1, 'Profesional ASST', 'PROFESIONAL_ASST');
MERGE INTO usuarios_sistema (id, correo_electronico, nombre_usuario, nombre_completo, fecha_registro, estado_usuario_id, fecha_creacion) VALUES (1, 'profesional@unicauca.edu.co', 'profesional_asst', 'Profesional ASST Test', TIMESTAMP '2024-01-01 00:00:00', 1, TIMESTAMP '2024-01-01 00:00:00');
MERGE INTO usuarios_sistema_roles (usuario_sistema_id, rol_id) KEY (usuario_sistema_id, rol_id) VALUES (1, 1);
