CREATE TABLE abstractoptions
(
  id bigint NOT NULL,
  CONSTRAINT abstractoptions_pkey PRIMARY KEY (id)
);
INSERT INTO
  abstractoptions(id)
SELECT
  id
  FROM options;

  ALTER TABLE options
    RENAME TO commerceoptions;

    ALTER TABLE realtyobject ADD COLUMN options_id bigint;

    update realtyobject ro  set options_id=(
    select options_id from commerce c where ro.id=c.id
    );
    ALTER TABLE commerce DROP COLUMN options_id;


--    INSERT INTO
--      abstractoptions(id)
--    SELECT
--      id
--      FROM LiveOptions;
--
--
--INSERT INTO
--      abstractoptions(id)
--    SELECT
--      id
--      FROM LiveSaleOptions;