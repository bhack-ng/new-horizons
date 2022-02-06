CREATE TABLE clienttask (
  id bigint NOT NULL,
  description character varying(1024),
  executiondatetime timestamp without time zone,
  client_id bigint,
  from_login character varying(255),
  to_login character varying(255),

  CONSTRAINT clienttask_pkey PRIMARY KEY (id),
    CONSTRAINT from_fkey FOREIGN KEY (from_login)
        REFERENCES agent (login) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT to_fkey FOREIGN KEY (to_login)
      REFERENCES agent (login) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT client_fkey FOREIGN KEY (client_id)
      REFERENCES client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);