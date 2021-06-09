import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main{

    public static Connection connection;
    public static String query;
    public static Statement statement;
    public static PreparedStatement pst;
    public static ResultSet rs;
	public static ReadValue rv = new ReadValue();
	public static WriteValue wv = new WriteValue();

	public static void main(String[] args)  throws SQLException{
		brukerGrensesnitt();
		//wv.leggTilAnbefaling("mai tai", "dry curaçao","likør","pierre Ferrand Dry Curacao");
		//wv.leggTilProdukt("pierre Ferrand Dry Curacao", "likør", "dry curaçao");
		//wv.leggTilMaaleenhet("l");
		//wv.leggTilDrink("gin and tonic", "bland sammen gin og tonic. hell over i et glass med is og limebiter.");
		//wv.leggTilIngrediens("angostura orange bitters", "bitter");
		//rv.hentAlleOppskrifter();
	}


	public static void brukerGrensesnitt() throws SQLException{

		avslutt:

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
					oppskrift();
					break;
				case "c":
					drinkFraIngredienser();
					break;
			}
		}
	}

	public static void drinkFraIngredienser(){
		ArrayList<String> ingredienser = new ArrayList<>();

		while(true){
			String in = getStrFromUser("\nf) Søk\nq) Avslutt\nIngrediens: ");
			if(in.equals("q")){
				return;
			} else if (in.equals("f")){
				rv.drinkFraIngredienser(ingredienser);
				System.out.println("\n ER BUTIKKEN FORTSATT ÅPEN?\n");
				System.out.println("\n DISSE MANGLER DU BARE 1 INGREDIENS TIL!\n");
				rv.enManglende(ingredienser);
				return;
			} else if (!rv.ingrediensFinnes(in)){
				System.out.println("\nDenne ingrediensen finnes ikke!\n");
				return;
			}
			ingredienser.add(in);
		}
	}

	public static void oppskrift() throws SQLException{
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
		wv.begin();
		wv.leggTilDrink(drink, beskrivelse);
		hentIngrediens(drink);
		System.out.println("Vil du legge til denne oppskriften?\n");
		wv.hentOppskrift(drink);
		String in = getStrFromUser("j) Ja\nn) Nei\n");
		if(in.equals("j")){
			System.out.println("Drink lagt til!");
			wv.commit();
			return;
		}

		System.out.println("Drink ikke lagt til!");
		wv.rollback();
		return;
	}

	public static void hentIngrediens(String drink) throws SQLException{
		int i = 1;
		while (true){
			String ingrediens = getStrFromUser("\nIngrediens nr: "+ i +  "\ningrediens: ");
			i = i+1;
			if (ingrediens.equals("q")){
				wv.rollback();
				brukerGrensesnitt();
			}
			if (!rv.ingrediensFinnes(ingrediens)){
				System.out.println("Denne ingrediensen finnes ikke allerede. Vennligst legg til ingredienstype");
				String ingredtype = getStrFromUser("\nIngredienstype: ");
				if (ingredtype.equals("q")){
					wv.rollback();
					brukerGrensesnitt();
				}
				wv.leggTilIngrediens(ingrediens, ingredtype);
			}
				String mengde = getStrFromUser("\nMengde med  " + ingrediens + ": ");
				if(mengde.equals("q")){
					wv.rollback();
					brukerGrensesnitt();
				}
				String maaleenhet = getStrFromUser("\nMaaleenhet: ");
				if (maaleenhet.equals("q")){
					wv.rollback();
					brukerGrensesnitt();
				}
				wv.leggTilMaaleenhet(maaleenhet);
				wv.leggTilOppskrift(drink, ingrediens, mengde, maaleenhet);
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

	public static String getStrFromUser(String tekst){
		Scanner s = new Scanner(System.in);
		System.out.print(tekst);
		return s.nextLine().trim().toLowerCase();
	}
}
