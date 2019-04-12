INSERT INTO users (id, username, password, enabled) VALUES('1ef601de-7508-4f43-a1b4-49062c68c86e', 'admin', '$2a$10$5NTnlNvnXLFXrXpj5qMEE.vSL.gFcDPWz7NJCtolmn3h0v7rb/r4G', TRUE); --admin
INSERT INTO users (id, username, password, enabled) VALUES('c15d3f75-c945-4cc8-b018-cf5525c0dc0c', 'stella', '$2a$10$jm7LYI78BtbjIOUgwPND3up6AM0MHYykPfwBG/I/roQ8g7CPLNuBS', TRUE); --stella

INSERT INTO authorities (userId, authority) VALUES('1ef601de-7508-4f43-a1b4-49062c68c86e', 'ADMIN');
INSERT INTO authorities (userId, authority) VALUES('1ef601de-7508-4f43-a1b4-49062c68c86e', 'USER');
INSERT INTO authorities (userId, authority) VALUES('c15d3f75-c945-4cc8-b018-cf5525c0dc0c', 'USER');