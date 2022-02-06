ALTER TABLE ExternalAgency ADD COLUMN friend BOOLEAN ;
UPDATE ExternalAgency SET friend= true;
ALTER TABLE ExternalAgency ALTER COLUMN friend SET NOT NULL;

