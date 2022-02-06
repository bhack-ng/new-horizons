ALTER TABLE realtyobject  ADD COLUMN publishYandexRealty BOOLEAN;
UPDATE realtyobject SET publishYandexRealty = FALSE;
ALTER  TABLE  realtyobject ALTER COLUMN publishYandexRealty SET NOT NULL;

