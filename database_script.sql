CREATE DATABASE drinks;
\c drinks;
BEGIN;

-- ================== LEGG TIL TABELLER ========================
CREATE TABLE IF NOT EXISTS cocktails (cid SERIAL PRIMARY KEY, cnavn VARCHAR(255) UNIQUE NOT NULL, beskrivelse TEXT);

CREATE TABLE IF NOT EXISTS ingredienstype (itid	SERIAL PRIMARY KEY, itnavn VARCHAR(255) UNIQUE NOT NULL);

CREATE TABLE IF NOT EXISTS maaleenhet (meid	SERIAL PRIMARY KEY, menavn VARCHAR(255) UNIQUE NOT NULL);

CREATE TABLE IF NOT EXISTS ingredienser (iid SERIAL PRIMARY KEY, inavn VARCHAR(255) UNIQUE NOT NULL,
											itid int REFERENCES ingredienstype(itid) NOT NULL);

CREATE TABLE IF NOT EXISTS produkt (pid SERIAL PRIMARY KEY,
									pnavn VARCHAR(255) UNIQUE NOT NULL,
									iid int REFERENCES ingredienser(iid) NOT NULL);

CREATE TABLE IF NOT EXISTS anbefaling (cid int REFERENCES cocktails(cid) NOT NULL,
									   pid int REFERENCES produkt(pid) NOT NULL);

CREATE TABLE IF NOT EXISTS oppskrift (cid int REFERENCES cocktails(cid) NOT NULL,
									  iid int REFERENCES ingredienser(iid) NOT NULL,
									  mengde VARCHAR(255) NOT NULL,
									  meid int REFERENCES maaleenhet(meid) NOT NULL);


-- ================== LEGG TIL VIEWS ========================
CREATE or REPLACE VIEW oppskrift_view (cnavn, inavn, mengde, pnavn, beskrivelse)
	AS
	SELECT cnavn, inavn, mengde || menavn mengde, pnavn, beskrivelse
		FROM oppskrift
			natural join cocktails
			natural join maaleenhet
			natural join ingredienser
			left outer join anbefaling
			natural join produkt
				ON produkt.iid = oppskrift.iid
				AND anbefaling.cid = oppskrift.cid
		ORDER BY cnavn DESC;

-- ================== INSERT VERDIER ========================

-- INGREDIENS_TYPE
INSERT INTO ingredienstype (itnavn) VALUES ('sprit');
INSERT INTO ingredienstype (itnavn) VALUES ('likør');
INSERT INTO ingredienstype (itnavn) VALUES ('meieriprodukt');
INSERT INTO ingredienstype (itnavn) VALUES ('sirup');
INSERT INTO ingredienstype (itnavn) VALUES ('grønnsak');


-- INGREDIENS
INSERT INTO ingredienser (inavn, itid) VALUES ('vodka',         (SELECT itid FROM ingredienstype WHERE itnavn = 'sprit'));
INSERT INTO ingredienser (inavn, itid) VALUES ('kaffelikør',    (SELECT itid FROM ingredienstype WHERE itnavn = 'likør'));
INSERT INTO ingredienser (inavn, itid) VALUES ('fløte',         (SELECT itid FROM ingredienstype WHERE itnavn = 'meieriprodukt'));
INSERT INTO ingredienser (inavn, itid) VALUES ('curaçao',       (SELECT itid FROM ingredienstype WHERE itnavn = 'likør'));
INSERT INTO ingredienser (inavn, itid) VALUES ('dry curaçao',   (SELECT itid FROM ingredienstype WHERE itnavn = 'likør'));
INSERT INTO ingredienser (inavn, itid) VALUES ('orgeat',        (SELECT itid FROM ingredienstype WHERE itnavn = 'sirup'));
INSERT INTO ingredienser (inavn, itid) VALUES ('demerara rum',  (SELECT itid FROM ingredienstype WHERE itnavn = 'sprit'));
INSERT INTO ingredienser (inavn, itid) VALUES ('jamaican rum',  (SELECT itid FROM ingredienstype WHERE itnavn = 'sprit'));
INSERT INTO ingredienser (inavn, itid) VALUES ('dark rum',      (SELECT itid FROM ingredienstype WHERE itnavn = 'sprit'));
INSERT INTO ingredienser (inavn, itid) VALUES ('lime',          (SELECT itid FROM ingredienstype WHERE itnavn = 'grønnsak'));
INSERT INTO ingredienser (inavn, itid) VALUES ('sukkerlake',    (SELECT itid FROM ingredienstype WHERE itnavn = 'sirup'));

-- PRODUKT
INSERT INTO produkt (pnavn, iid) VALUES ('kahlúa', 2);
INSERT INTO produkt (pnavn, iid) VALUES ('tia maria', 2);

-- COCKTAILS
INSERT INTO cocktails (cnavn, beskrivelse) VALUES('white russian', 'bland sammen kaffelikør og vodka over is. flyt fløten over med en skje.');
INSERT INTO cocktails (cnavn, beskrivelse) VALUES('black russian', 'bland sammen kaffelikør og vodka over is.');
INSERT INTO cocktails (cnavn, beskrivelse) VALUES('mai tai', 'putt alle ingredienser i en shaker og rist.');

-- MAALEENHET
INSERT INTO maaleenhet (menavn) VALUES('ml');
INSERT INTO maaleenhet (menavn) VALUES('cl');

-- ANBEFALING
INSERT INTO anbefaling (cid, pid) VALUES(1, 1);
INSERT INTO anbefaling (cid, pid) VALUES(1, 2);

-- OPPSKRIFT

-- WHITE RUSSIAN
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn  = 'white russian'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn  = 'vodka'),      60, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn  = 'white russian'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn  = 'kaffelikør'), 30, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn  = 'white russian'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn  = 'fløte'),      30, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
-- BLACK RUSSIAN
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn  = 'black russian'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn  = 'vodka'),      60, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn  = 'black russian'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn  = 'kaffelikør'), 30, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
-- MAI TAI -- 
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn = 'mai tai'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn = 'jamaican rum'), 30, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn = 'mai tai'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn = 'dry curaçao'), 15,  
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn = 'mai tai'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn = 'orgeat'), 15,       
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn = 'mai tai'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn = 'demerara rum'), 30, 
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn = 'mai tai'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn = 'lime'), 22,         
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
INSERT INTO oppskrift (cid, iid, mengde, meid) VALUES((SELECT cid  FROM cocktails    WHERE cnavn = 'mai tai'), 
                                                      (SELECT iid  FROM ingredienser WHERE inavn = 'sukkerlake'), 8,    
                                                      (SELECT meid FROM maaleenhet   WHERE menavn = 'ml'));
COMMIT;
