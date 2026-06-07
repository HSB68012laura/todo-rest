INSERT INTO user_entity (id, email, username, password, role) VALUES (NEXTVAL('user_entity_seq'), 'laura@dwes.net', 'laura', '{noop}12345', 'USER');
INSERT INTO user_entity (id, email, username, password, role) VALUES (NEXTVAL('user_entity_seq'), 'admin@dewes.net', 'admin', '{noop}admin123', 'ADMIN');
INSERT INTO user_entity (id, email, username, password, role) VALUES (NEXTVAL('user_entity_seq'), 'gestor@dewes.net', 'gestor', '{noop}gestor123', 'GESTOR');
INSERT INTO user_entity (id, email, username, password, role) VALUES (5, 'user@test.com', 'user', '{noop}1234', 'USER');

INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Estudiar para el exámen', 'ALTA', 'Repasar los ejercicios y leer el temario.', false);
INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 42, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Cena compañeros', 'MEDIA', 'Organización de la cena de fin de curso.', false);
INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 37, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Cambio de armario', 'MEDIA', 'Revisar la ropa de verano', false);
INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 39, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Recoger vasos', 'MEDIA', 'Recoger los nuevos vasos para la Entamá.', false);
INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 8, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Instalar SQL', 'ALTA', 'Instalar SQL Developer Edition en el 40.', false);
INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 226, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'GlutenApp', 'BAJA', 'Terminar la aplicación de GlutenApp.', false);
INSERT INTO task (created_at, deadline, id, title, priority, description, completed) VALUES (CURRENT_TIMESTAMP, TIMESTAMPADD(DAY, 13, CURRENT_TIMESTAMP), NEXTVAL('task_seq'), 'Lavadero', 'BAJA', 'Llevar el coche a lavar', false);

UPDATE task SET author_id = (SELECT id FROM user_entity WHERE username = 'laura');