CREATE TABLE client_dbfile (
  client_id bigint NOT NULL,
  files_id bigint NOT NULL,

  CONSTRAINT client_fk FOREIGN KEY (client_id)
      REFERENCES client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT file_fk FOREIGN KEY (files_id)
      REFERENCES dbfile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT files_id_const UNIQUE (files_id)
)