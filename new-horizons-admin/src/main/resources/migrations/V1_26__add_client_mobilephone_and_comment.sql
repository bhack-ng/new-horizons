ALTER TABLE client ADD COLUMN mobilephone varchar(255);

ALTER TABLE client ADD COLUMN comment varchar(1024);

ALTER TABLE client ADD CONSTRAINT mobilephone_uniq UNIQUE (mobilephone);

