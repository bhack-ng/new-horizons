ALTER TABLE contactsofowner RENAME COLUMN commerce_id to realtyobject_id;
ALTER TABLE contactsofowner DROP CONSTRAINT fki505ejc7k7j3jelktcaps6k2v;


ALTER TABLE recommendation RENAME COLUMN commerce_id to realtyobject_id;
ALTER TABLE recommendation DROP CONSTRAINT fk6kbikrue3infl8kb7ri0el1yn;




ALTER TABLE comment RENAME COLUMN commerce_id to realtyobject_id;

ALTER TABLE comment DROP CONSTRAINT fk6bl5a14ag73udfo47vph8rpmg;

