CREATE TABLE feed_errors
(
  feed_id bigint NOT NULL,
  errors character varying(255),
  CONSTRAINT fktgkdj78vghpd3lchyxiyhd1yj FOREIGN KEY (feed_id)
      REFERENCES feed (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE 
)
WITH (
  OIDS=FALSE
);
ALTER TABLE feed_errors
  OWNER TO postgres;

ALTER TABLE feed DROP COLUMN lasterror;