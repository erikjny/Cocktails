# Cocktails 
Dette er et program om cocktails

## Funksjoner
- Vise oversikt over cocktails
- Legge til nye oppskrifter
- Søke etter cocktails basert på hvilke ingredienser som er tilgjengelige

## Krav
For at programmet skal kunne kjøres krever det at følgende er på plass:
- Tilkobling til en database lokalt på maskinen
- Java JDK er installert

## Oppsett og kjøring
Klon prosjektet med følgende kommando:
- `git clone https://github.com/erikjny/Cocktails`
Naviger inn i den klonede mappen:
- `cd Cocktails`
Kjør programmet med denne kommandoen:
- `Javac *.java && java -cp "postgresql.jar:." Main`

## Innhold
Her er alle filene i prosjektet
- `Main.java`  inneholder Main
- `DBConnection.java`  er en klasse som gjør tilkoblingen til databasen enkel.
- `ReadValue.java`  er en klasse som leser data fra databasen.
- `WriteValue.java`  er en klasse som endrer data i databasen.
- `postgres.jar`  er en klasse-fil som trengs for tilkobling til databasen.
- `database_script.sql`  er et script som opretter databasen og setter inn data.

