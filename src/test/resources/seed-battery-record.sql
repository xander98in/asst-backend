MERGE INTO tipos_identificacion (id_tipo_identificacion, nombre, abreviatura) VALUES (1, 'Cedula de Ciudadania', 'CC');
MERGE INTO estados_persona_evaluada (id_estado_persona_evaluada, nombre) VALUES (1, 'Sin registro');
MERGE INTO estados_persona_evaluada (id_estado_persona_evaluada, nombre) VALUES (2, 'Con registro');
MERGE INTO estados_registro_gestion_baterias (id_estado_registro_gestion_bateria, nombre) VALUES (1, 'Creado');
MERGE INTO estados_registro_gestion_baterias (id_estado_registro_gestion_bateria, nombre) VALUES (2, 'En diligenciamiento');
MERGE INTO estados_registro_gestion_baterias (id_estado_registro_gestion_bateria, nombre) VALUES (3, 'Diligenciado');
MERGE INTO estados_registro_gestion_baterias (id_estado_registro_gestion_bateria, nombre) VALUES (4, 'Cerrado');
