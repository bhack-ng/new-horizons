CREATE TABLE directionroad
(
  id bigint NOT NULL,
  avitoid integer NOT NULL,
  cityname character varying(255),
  regionname character varying(255),
  roadname character varying(255),
  CONSTRAINT directionroad_pkey PRIMARY KEY (id)
);


CREATE TABLE steadsale
(
  distancetocity integer,
  objecttype character varying(255),
  propertyright character varying(255),
  id bigint NOT NULL,
  directionroad_id bigint,
  CONSTRAINT steadsale_pkey PRIMARY KEY (id),
  CONSTRAINT fkktaixckdggqkk6146037i2whg FOREIGN KEY (directionroad_id)
      REFERENCES directionroad (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fksjtj87k6ig8x9706pbg81ralh FOREIGN KEY (id)
      REFERENCES realtyobject (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



  CREATE TABLE steadrent
(
  distancetocity integer,
  objecttype character varying(255),
  propertyright character varying(255),
  id bigint NOT NULL,
  directionroad_id bigint,
  CONSTRAINT steadrent_pkey PRIMARY KEY (id),
  CONSTRAINT fk6r07dbrqfof7newk4l9dtwm9q FOREIGN KEY (directionroad_id)
      REFERENCES directionroad (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkitx88mhvjlujqxknqwt2fyasg FOREIGN KEY (id)
      REFERENCES realtyobject (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE TABLE steadarea
(
  lendarea double precision,
  id bigint NOT NULL,
  CONSTRAINT steadarea_pkey PRIMARY KEY (id),
  CONSTRAINT fkkidxm2p9rvxg9g3mugkmly9dy FOREIGN KEY (id)
      REFERENCES abstractarea (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);




CREATE TABLE privatehousesale
(
  distancetocity integer,
  propertyright character varying(255),
  wallstype character varying(255),
  id bigint NOT NULL,
  directionroad_id bigint,
  CONSTRAINT privatehousesale_pkey PRIMARY KEY (id),
  CONSTRAINT fki4gnnde5ned6acrpp1oxu999q FOREIGN KEY (directionroad_id)
      REFERENCES directionroad (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkr2ky98eqxucwmjhsybykpjvof FOREIGN KEY (id)
      REFERENCES realtyobject (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE privatehouserent
(
  distancetocity integer,
  propertyright character varying(255),
  wallstype character varying(255),
  id bigint NOT NULL,
  directionroad_id bigint,
  CONSTRAINT privatehouserent_pkey PRIMARY KEY (id),
  CONSTRAINT fknek81vdqb559mi3s37v74phbs FOREIGN KEY (id)
      REFERENCES realtyobject (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkqsq6rayn8jqiay4045rngpqa7 FOREIGN KEY (directionroad_id)
      REFERENCES directionroad (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);