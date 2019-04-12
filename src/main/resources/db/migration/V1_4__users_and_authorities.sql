CREATE TABLE users (
  id CHAR(36) PRIMARY KEY,
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
  userId CHAR(36) NOT NULL,
  authority VARCHAR(256),

  PRIMARY KEY (userId, authority),
  FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);