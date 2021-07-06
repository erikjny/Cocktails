# Cocktails 
Dette er et program som lar deg holde styr på cocktail-oppskrifter gjennom et simpelt terminalbasert GUI.

## Funksjoner
- Vise oversikt over cocktails
- Legge til nye oppskrifter
- Søke etter cocktails basert på hvilke ingredienser som er tilgjengelige

## Krav
For at programmet skal kunne kjøres krever det at følgende er på plass:
- Tilkobling til en database lokalt på maskinen, (se Konfigurering).
- Java JDK er installert

## Oppsett og kjøring
Klon prosjektet og naviger inn i mappen:
```sh
git clone https://github.com/erikjny/Cocktails
cd Cocktails
```

Kjør programmet med denne kommandoen:
```sh
Javac *.java && java -cp "postgresql.jar:." Main
```

## Innhold
Her er alle filene i prosjektet
- `Main.java`  inneholder Main
- `DBConnection.java`  er en klasse som gjør tilkoblingen til databasen enkel.
- `ReadValue.java`  er en klasse som leser data fra databasen.
- `WriteValue.java`  er en klasse som endrer data i databasen.
- `postgres.jar`  er en klasse-fil som trengs for tilkobling til databasen.
- `database_script.sql`  er et script som opretter databasen og setter inn data.

## Konfigurering
Dersom du har tilkobling til database klar, må du kanskje endre disse kodelinjene i klassen `DBConnection.java`.
```java
    private String user = "[ditt brukernavn]"; // f.eks "eriknystad"
    // Dersom du er koblet på en database med et passord
    private String pwd = "";
    // Dersom du bruker en annen port
    private String port = "5432/";
```


## Sette opp tilkobling til database
Du kan sette opp en tilkobling til en postgres-database på denne måten:

1. Installer brew
```sh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```
2. Installer postgres
```sh
brew install postgresql
brew services start postgresql
```
3. Kjør scriptet som oppretter databasen, tabeller og views og deretter setter inn litt data
3.1 ```sh psql postgres ```
3.2 ```sh \i database_script.sql ```
