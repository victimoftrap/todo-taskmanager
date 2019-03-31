ALTER TABLE tasks ADD COLUMN updatedAt VARCHAR NULL;
UPDATE tasks SET updatedAt = createdAt WHERE updatedAt IS NULL;
ALTER TABLE tasks ALTER COLUMN updatedAt SET NOT NULL;
