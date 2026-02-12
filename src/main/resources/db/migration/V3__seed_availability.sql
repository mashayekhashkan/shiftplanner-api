ALTER TABLE store
  ADD COLUMN IF NOT EXISTS timezone varchar(255);

UPDATE store
SET timezone = 'Europe/Vienna'
WHERE timezone IS NULL;

ALTER TABLE store
  ALTER COLUMN timezone SET NOT NULL;