CREATE TABLE abstractbuilding
(
  id bigint NOT NULL,
  floor integer ,
  floortotal integer ,
  CONSTRAINT abstractbuilding_pkey PRIMARY KEY (id)
);

INSERT INTO
  abstractbuilding(id, floor, floortotal)
SELECT
  id, floor, floor_total
  FROM building;

  ALTER TABLE building
    RENAME TO commercebuilding;



    ALTER TABLE realtyobject ADD COLUMN building_id bigint;

    update realtyobject ro  set building_id=(
    select building_id from commerce c where ro.id=c.id
    );
    ALTER TABLE commerce DROP COLUMN building_id;
    ALTER TABLE commercebuilding DROP COLUMN floor;
    ALTER TABLE commercebuilding DROP COLUMN floor_total;

--
--INSERT INTO
--  abstractbuilding(id, floor, floortotal)
--SELECT
--  id, floor, floortotal
--  FROM DwellingHouse;
--
--  update realtyobject ro  set building_id=(
--      select building_id from liverealty c where ro.id=c.id
--      );
--      ALTER TABLE liverealty DROP COLUMN building_id;
--      ALTER TABLE DwellingHouse DROP COLUMN floor;
--      ALTER TABLE DwellingHouse DROP COLUMN floortotal;
