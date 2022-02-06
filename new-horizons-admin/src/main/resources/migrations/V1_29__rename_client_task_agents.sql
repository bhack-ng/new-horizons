ALTER TABLE clienttask RENAME COLUMN from_login TO creator_login;

ALTER TABLE clienttask RENAME COLUMN to_login TO executor_login;