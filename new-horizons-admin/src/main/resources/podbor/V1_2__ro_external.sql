ALTER TABLE realtyobject ADD COLUMN external boolean;
update realtyobject set external=false;
ALTER TABLE realtyobject ALTER COLUMN external SET NOT NULL;

ALTER TABLE realtyfilter ADD COLUMN external boolean;
update realtyfilter set external=false;
ALTER TABLE realtyfilter ALTER COLUMN external SET NOT NULL;

select count(ro) from ru.simplex_software.arbat_baza.model.RealtyObject ro  where   ro.realtyObjectType='COMMERCE_SALE'  AND (ro.externalFeed not null) =false ]