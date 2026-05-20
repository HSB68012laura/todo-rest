INSERT INTO task (created_at, deadline, id, title, priority, description)  VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Estudiar para el examen', 'Alta', 'Repasar los ejercicios y leer el temario.');
INSERT INTO task (created_at, deadline, id, title, priority, description)  VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 42, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Cena compañerps', 'Media', 'Organización de la cena de fin de curso.');
INSERT INTO task (created_at, deadline, id, title, priority, description) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 37, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Cambio de armario', 'Media', 'Revisar la ropa de verano');
INSERT INTO task (created_at, deadline, id, title, priority, description) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 39, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Recoger vasos', 'Media', 'Recoger los nuevos vasos para la Entamá.');
INSERT INTO task (created_at, deadline, id, title, priority, description) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 8, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Instalar SQL', 'Alta', 'Instalar SQL Developed Edition en el 40.');
INSERT INTO task (created_at, deadline, id, title, priority, description) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 226, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'GlutenApp', 'Baja', 'Terminar la aplicación de GlutenApp.');
INSERT INTO task (created_at, deadline, id, title, priority, description) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 13, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Lavadero', 'Baja', 'Llevar el coche a Lavar');


INSERT INTO user_entity (id, email, username, password, rol) VALUES (NEXTVAL('user_entity_seq'), 'laura@dwes.net','laura','{noop}12345','USER');
INSERT INTO user_entity (id, email, username, password, rol) VALUES (NEXTVAL('user_entity_seq'), 'admin@dewes.net','admin','{noop}12345','ADMIN');
INSERT INTO user_entity (id, email, username, password, rol) VALUES (NEXTVAL('user_entity_seq'), 'gestor@dewes.net','gestor','{noop}12345','GESTOR');

UPDATE task SET author_id = CURRVAL('user_entity_seq');