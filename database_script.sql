BEGIN;

-- ================== LEGG TIL TABELLER ========================
CREATE TABLE IF NOT EXISTS cocktails (cid SERIAL PRIMARY KEY, cnavn VARCHAR(255) UNIQUE NOT NULL, beskrivelse TEXT);
CREATE TABLE IF NOT EXISTS produkt (pid SERIAL PRIMARY KEY, pnavn VARCHAR(255) UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS ingredienstype (itid	SERIAL PRIMARY KEY, itnavn VARCHAR(255) UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS maaleenhet (meid	SERIAL PRIMARY KEY, menavn VARCHAR(255) UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS ingredienser (iid SERIAL PRIMARY KEY, inavn VARCHAR(255) UNIQUE NOT NULL,
											itid int REFERENCES ingredienstype(itid));
CREATE TABLE IF NOT EXISTS anbefaling (cid int REFERENCES cocktails(cid), iid int REFERENCES ingredienser(iid),
											pid int REFERENCES produkt(pid));
CREATE TABLE IF NOT EXISTS oppskrift (cid int REFERENCES cocktails(cid), iid int REFERENCES ingredienser(iid),
											mengde VARCHAR(255) NOT NULL, meid int REFERENCES maaleenhet(meid));


-- ================== INSERT VERDIER ========================

-- INGREDIENS_TYPE
-- INSERT INTO ingredienstype (itnavn) VALUES ('sprit');
-- INSERT INTO ingredienstype (itnavn) VALUES ('likør');
-- INSERT INTO ingredienstype (itnavn) VALUES ('meieriprodukt');

-- INGREDIENS
-- INSERT INTO ingredienser (inavn, itid) VALUES('vodka', 1);
-- INSERT INTO ingredienser (inavn, itid) VALUES('kaffelikør', 2);
-- INSERT INTO ingredienser (inavn, itid) VALUES('fløte', 3);

-- PRODUKT
-- INSERT INTO produkt (pnavn) VALUES ('kahlúa');
-- INSERT INTO produkt (pnavn) VALUES ('tia maria');

-- COCKTAILS
-- INSERT INTO cocktails (cnavn, beskrivelse) VALUES('white russian', 'bland sammen kaffelikør og vodka over is. flyt fløten over med en skje.');
-- INSERT INTO cocktails (cnavn, beskrivelse) VALUES('black russian', 'bland sammen kaffelikør og vodka over is.');

-- MAALEENHET
-- INSERT INTO maaleenhet (menavn) VALUES('ml');
-- INSERT INTO maaleenhet (menavn) VALUES('cl');

-- ANBEFALING
-- INSERT INTO anbefaling (cid, iid, pid) VALUES(1, 2, 1);
-- INSERT INTO anbefaling (cid, iid, pid) VALUES(1, 2, 2);

-- OPPSKRIFT
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(1, 1, 60, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(1, 2, 30, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(1, 3, 30, 1);

COMMIT;
