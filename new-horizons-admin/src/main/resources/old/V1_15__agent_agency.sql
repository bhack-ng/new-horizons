ALTER TABLE realtyobject
   ALTER COLUMN note TYPE character varying(4096);
update agent set agency_id=(select id from agency);