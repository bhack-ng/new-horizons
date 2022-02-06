ALTER TABLE floorscheme RENAME COLUMN data TO data_id;

ALTER TABLE floorscheme ALTER COLUMN data_id TYPE BIGINT;

ALTER TABLE floorscheme ADD FOREIGN KEY (data_id) REFERENCES photodata;