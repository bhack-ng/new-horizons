ALTER TABLE realtyobject ADD COLUMN publishavito BOOLEAN;
UPDATE realtyobject SET publishavito = FALSE ;
ALTER  TABLE  realtyobject ALTER COLUMN publishavito SET NOT NULL;