CREATE TABLE client (
  id bigint NOT NULL,
  email character varying(255),
  name character varying(255),
  phone character varying(255),

  CONSTRAINT client_pkey PRIMARY KEY (id),
  CONSTRAINT email_uniq UNIQUE (email),
  CONSTRAINT phone_uniq UNIQUE (phone)
);

CREATE TABLE juridicalclient (
  id bigint NOT NULL,
  inn character varying(255),
  ogrn character varying(255),
  shortname character varying(255),

  CONSTRAINT jClient_pkey PRIMARY KEY (id),
  CONSTRAINT client_fkey FOREIGN KEY (id)
      REFERENCES client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT inn_uniq UNIQUE (inn),
  CONSTRAINT ogrn_uniq UNIQUE (ogrn)
);

CREATE TABLE naturalclient (
  id bigint NOT NULL,

  CONSTRAINT nClient_pkey PRIMARY KEY (id),
  CONSTRAINT client_fkey FOREIGN KEY (id)
      REFERENCES client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);