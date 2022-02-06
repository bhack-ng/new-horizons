ALTER TABLE realtyobject ADD COLUMN renter_id bigint;

ALTER TABLE realtyobject ADD CONSTRAINT renter_const FOREIGN KEY (renter_id)
      REFERENCES client (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;