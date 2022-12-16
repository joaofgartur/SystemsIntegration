CREATE TABLE IF NOT EXISTS "location" (ID serial PRIMARY KEY, "locationName" VARCHAR (255));
CREATE TABLE IF NOT EXISTS "station" (ID serial PRIMARY KEY, "stationName" VARCHAR (255));

INSERT INTO "location" VALUES (1, 'Alta');
INSERT INTO "location" VALUES (2, 'Baixa');
INSERT INTO "location" VALUES (3, 'Polo II');
INSERT INTO "location" VALUES (4, 'Norton');

INSERT INTO "station" VALUES (1, 'Coimbra');
INSERT INTO "station" VALUES (2, 'Lisboa');
INSERT INTO "station" VALUES (3, 'Porto');

-- Add Constraints
ALTER TABLE "requirement1Table" ADD CONSTRAINT req1 UNIQUE ("station");
ALTER TABLE "requirement2Table" ADD CONSTRAINT req2 UNIQUE ("location");
ALTER TABLE "requirement3Table" ADD CONSTRAINT req3 UNIQUE ("station");
ALTER TABLE "requirement4Table" ADD CONSTRAINT req4 UNIQUE ("location");
ALTER TABLE "requirement5Table" ADD CONSTRAINT req5 UNIQUE ("station");
ALTER TABLE "requirement6Table" ADD CONSTRAINT req6 UNIQUE ("alertType");
ALTER TABLE "requirement7Table" ADD CONSTRAINT req7 UNIQUE ("redAlertStation");
ALTER TABLE "requirement8Table" ADD CONSTRAINT req8 UNIQUE ("location");
ALTER TABLE "requirement9Table" ADD CONSTRAINT req9 UNIQUE ("station");
ALTER TABLE "requirement10Table" ADD CONSTRAINT req10 UNIQUE ("station");
ALTER TABLE "requirement11Table" ADD CONSTRAINT req11 UNIQUE ("station");