ALTER TABLE tasks ALTER COLUMN createdAt TYPE TIMESTAMPTZ USING createdAt::timestamptz AT TIME ZONE 'UTC';
ALTER TABLE tasks ALTER COLUMN updatedAt TYPE TIMESTAMPTZ USING updatedAt::timestamptz AT TIME ZONE 'UTC';
SET TimeZone = 'UTC';