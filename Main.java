import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{

	public static ReadValue rv = new ReadValue();
	public static WriteValue wv = new WriteValue();

	public static void main(String[] args)  throws SQLException{
		brukerGrensesnitt();
	}

	public static void brukerGrensesnitt() throws SQLException{

        while (true) {
            System.out.println("\n<<< ----- HOVEDMENY ----- >>>");
            System.out.println("Hva ønsker du å gjøre?\n");
			System.out.format("\n%5s%25s", "q)", "avslutte programmet");
			System.out.format("\n%5s%25s", "a)", "se alle oppskrifter");
			System.out.format("\n%5s%25s", "b)", "opprett ny oppskrift");
			System.out.format("\n%5s%25s", "c)", "søk med ingredienser");
            String in = getStrFromUser("\n\n");

            switch (in) {
                case "q":
					System.exit(0);
				case "a":
					rv.hentAlleOppskrifter();
					break;
				case "b":
					opprettOppskrift();
					break;
				case "c":
					drinkFraIngredienser();
					break;
			}
		}
	}

	// Henter drinks som kan lages med tilgjengelige ingredienser
	public static void drinkFraIngredienser(){
		ArrayList<String> ingredienser = new ArrayList<>();

		while(true){
			// Henter ingrediens fra bruker
			String in = getStrFromUser("\nf) Søk\nq) Avslutt\nIngrediens: ");
			if(in.equals("q")){
				return;
			} else if (in.equals("f")){
				// Henter drinks med tilgjengelige ingredienser dersom det finnes noen
				if (!rv.drinkFraIngredienser(ingredienser)){
					System.out.println("\nOBS! INGREN OPPSKRIFTER MED DISSE INGREDIENSENE\n");
				}
				// Henter drinks hvor én ingrdiens mangler
				rv.enManglende(ingredienser);
				return;
			} else if (!rv.ingrediensFinnes(in)){
				System.out.println("\nDenne ingrediensen finnes ikke!\n");
				return;
			}
			ingredienser.add(in);
		}
	}

	// Oppretter ny drink med beskrivelse og legger dette i databasen
	public static void opprettOppskrift() throws SQLException{
		System.out.println("\nq) Hovedmeny\n\nOPPRETTER NY DRINK\n");
		String drink = getStrFromUser("Drinkens navn: ");

		if (drink.equals("q")){return;}
		if (rv.drinkFinnes(drink)){
			System.out.println("\nDenne finnes allerede!\n");
			return;
		}
		String beskrivelse = getStrFromUser("\nKort beskrivelse: ");
		if (beskrivelse.equals("q")){return;}

		// BEGYNN Å SETTE INN VERDIER
		// ruller tilbake dersom noe går galt
		wv.begin();

		// Legger drink til databasen med beskrivelse
		wv.leggTilDrink(drink, beskrivelse);
		// Henter ingredienser fra bruker
		hentIngredienser(drink);
		System.out.println("Vil du legge til denne oppskriften?\n");
		wv.hentOppskrift(drink);
		String in = getStrFromUser("j) Ja\nn) Nei\n");
		if(in.equals("j")){
			System.out.println("Drink lagt til!");
			// Fullfører innleggingen i databasen
			wv.commit();
			return;
		}
		System.out.println("Drink ikke lagt til!");
		wv.rollback();
		return;
	}

	// Henter ingredienser fra bruker og sender informasjonen til WriteValue-klassen
 	public static void hentIngredienser(String drink) throws SQLException{
		int i = 1;
		while (true){
			// Henter ingrediens
			String ingrediens = getStrFromUser("\nIngrediens nr: "+ (i++) +  "\ningrediens: ");
			if (ingrediens.equals("q")){
				wv.rollback();
				brukerGrensesnitt();
			}
			// Sjekker om ingrediensen finnes allerede
			if (!rv.ingrediensFinnes(ingrediens)){
				System.out.println("Denne ingrediensen finnes ikke allerede. Vennligst legg til ingredienstype");
				String ingredtype = getStrFromUser("\nIngredienstype: ");
				// Legger til ingredienstype mm bruker avslutter programmet
				if (ingredtype.equals("q")){
					wv.rollback();
					brukerGrensesnitt();
				}
				wv.leggTilIngrediens(ingrediens, ingredtype);
			}

			// Henter mengde
			String mengde = getStrFromUser("\nMengde med  " + ingrediens + ": ");
			if(mengde.equals("q")){
				wv.rollback();
				brukerGrensesnitt();
			}

			// Henter måleenhet
			String maaleenhet = getStrFromUser("\nMaaleenhet: ");
			if (maaleenhet.equals("q")){
				wv.rollback();
				brukerGrensesnitt();
			}
			// Legger til maaleenheten i databasen og oppretter deretter oppskriften
			wv.leggTilMaaleenhet(maaleenhet);
			wv.leggTilOppskrift(drink, ingrediens, mengde, maaleenhet);

			// Printer valg
			System.out.format("\n%5s%10s", "f)", "fullfør");
			System.out.format("\n%5s%10s", "q)", "avbryt");
			System.out.format("\n%5s%10s", "↵)", "fortsett");
			String svar = getStrFromUser("\n");

			if(svar.equals("q")){
				wv.rollback();
				brukerGrensesnitt();
			}else if (svar.isEmpty()){
				continue;
			}else if (svar.equals("f")){
				break;
			}
		}
	}

	// Printer ønsket melding og returnerer input fra bruker
	public static String getStrFromUser(String tekst){
		Scanner s = new Scanner(System.in);
		System.out.print(tekst);
		return s.nextLine().trim().toLowerCase();
	}
}
