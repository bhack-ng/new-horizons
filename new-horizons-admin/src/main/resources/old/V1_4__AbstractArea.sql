CREATE TABLE abstractarea
(
  id bigint NOT NULL,
  rooms character varying(255),
  roomscount integer,
  total double precision,
  CONSTRAINT abstractarea_pkey PRIMARY KEY (id)
);

INSERT INTO
  abstractarea (id,rooms, roomscount, total)
SELECT
  id, rooms, rooms_count, total
  FROM area;
 ALTER TABLE area DROP COLUMN rooms;
ALTER TABLE area DROP COLUMN rooms_count;
ALTER TABLE area DROP COLUMN total;


ALTER TABLE photo DROP CONSTRAINT fkaajix0kvwfnsrdu6lpbxrmrov;
