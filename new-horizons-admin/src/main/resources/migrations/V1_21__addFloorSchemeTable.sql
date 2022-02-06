CREATE TABLE floorscheme (
  id bigint NOT NULL,
  data oid,
  name character varying(255),
  CONSTRAINT floorscheme_pkey PRIMARY KEY (id)
);
ALTER TABLE realtyobject ADD COLUMN floorscheme_id BIGINT;
ALTER TABLE realtyobject ADD FOREIGN KEY (floorscheme_id) REFERENCES floorscheme;
CREATE TABLE realtyobject_coords (
  realtyobject_id bigint NOT NULL,
  coords integer
);