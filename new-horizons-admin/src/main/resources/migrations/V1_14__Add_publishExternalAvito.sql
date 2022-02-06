ALTER TABLE realtyobject ADD COLUMN publishexternalavito BOOLEAN;
        UPDATE realtyobject SET publishexternalavito = FALSE ;
        ALTER  TABLE  realtyobject ALTER COLUMN publishexternalavito SET NOT NULL;