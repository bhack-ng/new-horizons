CREATE TABLE IF NOT EXISTS  ObjectStatus (
  id bigint NOT NULL,
  name character varying(255),
  CONSTRAINT objectstatus_pkey PRIMARY KEY (id)
);
INSERT INTO objectstatus(id, name) VALUES (1, 'Рекламируется');
INSERT INTO objectstatus(id, name) VALUES (2, 'Свободен');
INSERT INTO objectstatus(id, name) VALUES (3, 'Сдан');
INSERT INTO objectstatus(id, name) VALUES (4, 'Некачественный лот');
INSERT INTO objectstatus(id, name) VALUES (5, 'Недозвон');
INSERT INTO objectstatus(id, name) VALUES (6, 'Думает');