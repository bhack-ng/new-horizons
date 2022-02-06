CREATE TABLE objectstatus (
    id bigint NOT NULL,
    name character varying(255),
    color character varying(255)
);
INSERT INTO objectstatus (id, name, color) VALUES (4, 'Некачественный лот', '#fdb4af');
INSERT INTO objectstatus (id, name, color) VALUES (1, 'Рекламируется', '#b7f769');
INSERT INTO objectstatus (id, name, color) VALUES (3, 'Сдан', '#c5dafc');
INSERT INTO objectstatus (id, name, color) VALUES (2, 'Свободен', '#faf07f');
INSERT INTO objectstatus (id, name, color) VALUES (5, 'Недозвон', '#fbe58f');
INSERT INTO objectstatus (id, name, color) VALUES (6, 'Думает', '#feffb0');


INSERT INTO cianadminarea(id, fiasaoguid, name)VALUES (1, '5c8b06f1-518e-496e-b683-7bf917e0d70b', 'Москва');
INSERT INTO cianadminarea(id, fiasaoguid, name)VALUES (2, 'd286798f-0849-4a7c-8e78-33c88dc964c6', 'Московская область');
