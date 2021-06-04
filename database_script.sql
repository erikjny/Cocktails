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


-- ================== LEGG TIL VIEWS ========================
CREATE VIEW oppskrift_view (cnavn, inavn, mengde, pnavn, beskrivelse)
AS
SELECT cnavn, inavn, mengde || menavn mengde, pnavn, beskrivelse
FROM oppskrift
natural join cocktails
natural join ingredienser
natural join maaleenhet
left outer join anbefaling
natural join produkt ON anbefaling.iid = oppskrift.iid AND anbefaling.cid = oppskrift.cid
ORDER BY cnavn DESC;

-- ================== INSERT VERDIER ========================

-- INGREDIENS_TYPE
-- INSERT INTO ingredienstype (itnavn) VALUES ('sprit');
-- INSERT INTO ingredienstype (itnavn) VALUES ('likør');
-- INSERT INTO ingredienstype (itnavn) VALUES ('meieriprodukt');
-- INSERT INTO ingredienstype (itnavn) VALUES ('sirup');
-- INSERT INTO ingredienstype (itnavn) VALUES ('grønnsak');

-- INGREDIENS
-- INSERT INTO ingredienser (inavn, itid) VALUES('vodka', 1);
-- INSERT INTO ingredienser (inavn, itid) VALUES('kaffelikør', 2);
-- INSERT INTO ingredienser (inavn, itid) VALUES('fløte', 3);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('rom', 1);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('curaçao', 2);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('dry curaçao', 2);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('orgeat', 7);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('demerara rum', 1);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('jamaican rum', 1);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('dark rum', 1);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('lime', 8);
-- INSERT INTO ingredienser (inavn, itid) VALUES ('sukkerlake', 7);

-- PRODUKT
-- INSERT INTO produkt (capnavn) VALUES ('kahlúa');
-- INSERT INTO produkt (pnavn) VALUES ('tia maria');

-- COCKTAILS
-- INSERT INTO cocktails (cnavn, beskrivelse) VALUES('white russian', 'bland sammen kaffelikør og vodka over is. flyt fløten over med en skje.');
-- INSERT INTO cocktails (cnavn, beskrivelse) VALUES('black russian', 'bland sammen kaffelikør og vodka over is.');
-- INSERT INTO cocktails (cnavn, beskrivelse) VALUES('mai tai', 'putt alle ingredienser i en shaker og rist.');

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

-- MAI TAI
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 12, 30, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 11, 15, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 10, 15, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 13, 30, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 9, 22, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 15, 8, 1);
-- INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES(3, 14, 15, 1);

COMMIT;
