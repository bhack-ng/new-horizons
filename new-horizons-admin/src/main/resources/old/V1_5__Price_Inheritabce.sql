CREATE TABLE simpleprice
(
  id bigint NOT NULL,
  currency character varying(255),
  value integer,
  CONSTRAINT simpleprice_pkey PRIMARY KEY (id)
);

INSERT INTO
  simpleprice(id,currency, value )
SELECT
  id,currency, value
  FROM price;
  ALTER TABLE price DROP COLUMN currency;
  ALTER TABLE price DROP COLUMN value;

  ALTER TABLE price
    RENAME TO commerceprice