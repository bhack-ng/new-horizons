ALTER TABLE realtyobject ADD COLUMN area_id bigint;

UPDATE realtyobject ro set area_id =(
    select  c.area_id FROM commerce c where ro.id=c.id
);
--UPDATE realtyobject ro set area_id =(
--    select  lr.area_id FROM liverealty lr where ro.id=lr.id
--);
ALTER TABLE commerce DROP COLUMN area_id;
--ALTER TABLE liverealty DROP COLUMN area_id;



ALTER TABLE realtyobject ADD COLUMN price_id bigint;

UPDATE realtyobject ro set price_id =(
    select  c.price_id FROM commerce c where ro.id=c.id
);

ALTER TABLE commerce DROP COLUMN price_id;