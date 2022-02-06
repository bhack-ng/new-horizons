CREATE TABLE IF NOT EXISTS  realtyobject
(
  id bigint NOT NULL,
  nextcall date,
  premiumincian boolean NOT NULL,
  publishcian boolean NOT NULL,
  publishsite boolean NOT NULL,
  agent_login character varying(255),
  commission_id bigint,
  metrolocation_id bigint,
  objectstatus_id bigint,
  address_id bigint,
  realtyobjecttype character varying(255),
  linktocian character varying(255),
  note character varying(2048),
  CONSTRAINT realtyobject_pkey PRIMARY KEY (id)
);

INSERT INTO
  realtyobject (id,premiumincian,publishcian,publishsite,nextcall,agent_login,commission_id,metrolocation_id,objectstatus_id,realtyobjecttype,  address_id,linktocian, note)
SELECT
  id,premiumincian,publishcian,publishsite,nextcall,agent_login,commission_id,metrolocation_id,objectstatus_id, realtyobjecttype ,address_id, linktocian, note
  FROM commerce;

ALTER TABLE commerce DROP COLUMN premiumincian;
ALTER TABLE commerce DROP COLUMN publishcian;
ALTER TABLE commerce DROP COLUMN publishsite;
ALTER TABLE commerce DROP COLUMN nextcall;
ALTER TABLE commerce DROP COLUMN agent_login;
ALTER TABLE commerce DROP COLUMN commission_id;
ALTER TABLE commerce DROP COLUMN metrolocation_id;
ALTER TABLE commerce DROP COLUMN objectstatus_id;
ALTER TABLE commerce DROP COLUMN realtyobjecttype;
ALTER TABLE commerce DROP COLUMN address_id;
ALTER TABLE commerce DROP COLUMN linktocian;
ALTER TABLE commerce DROP COLUMN note;


ALTER TABLE photo RENAME COLUMN commerce_id to realtyobject_id;


--  --ALTER TABLE  realtyobject add column realtyobjecttype  character varying(255);
--  --update realtyobject ro SET realtyobjecttype=(
--  --	select  realtyobjecttype FROM commerce c where ro.id=c.id
--  --)


