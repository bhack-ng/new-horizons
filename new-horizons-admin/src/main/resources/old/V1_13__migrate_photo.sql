ALTER TABLE photo RENAME data  TO data1;
ALTER TABLE photo ADD COLUMN data_id bigint;
ALTER TABLE photo ADD COLUMN preview_id bigint;

CREATE TABLE photodata
(
  id bigint NOT NULL,
  data oid,
  photo_id bigint,
  CONSTRAINT photodata_pkey PRIMARY KEY (id)
);

--Photo
with pt  AS (SELECT * from Photo p where p.data_id is null )
INSERT INTO PhotoData(id, data, photo_id)  select nextval('hibernate_sequence') , pt.data1, pt.id FROM pt;
update Photo AS p
SET data_id = pd.id from PhotoData pd where pd.photo_id=p.id;

--Preview
with pv  AS (SELECT * from Photo p where p.preview_id is null )
INSERT INTO PhotoData(id, data, photo_id)  select nextval('hibernate_sequence') , null, pv.id FROM pv;
update Photo AS p
SET preview_id = pd.id from PhotoData pd where pd.photo_id=p.id;

ALTER TABLE photodata DROP COLUMN photo_id;
ALTER TABLE photo DROP COLUMN data1;


