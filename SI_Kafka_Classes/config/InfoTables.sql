CREATE TABLE IF NOT EXISTS "location" (ID serial PRIMARY KEY, "locationName" VARCHAR (255));
CREATE TABLE IF NOT EXISTS "station" (ID serial PRIMARY KEY, "stationName" VARCHAR (255));

INSERT INTO "location" VALUES (1, 'Alta');
INSERT INTO "location" VALUES (2, 'Baixa');
INSERT INTO "location" VALUES (3, 'Polo II');
INSERT INTO "location" VALUES (4, 'Norton');

INSERT INTO "station" VALUES (1, 'Coimbra');
INSERT INTO "station" VALUES (2, 'Lisboa');
INSERT INTO "station" VALUES (3, 'Porto');