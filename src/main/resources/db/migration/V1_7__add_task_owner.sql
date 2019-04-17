ALTER TABLE tasks ADD COLUMN owner CHAR(36);
UPDATE tasks SET owner = (SELECT id FROM users WHERE username = 'admin') WHERE owner IS NULL;
ALTER TABLE tasks ALTER COLUMN owner SET NOT NULL;