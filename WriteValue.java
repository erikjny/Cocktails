import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WriteValue{
    private Statement statement;
    private ResultSet rs;
	private DBConnection dbc = new DBConnection();
	private Connection connection = dbc.getConnection();
	private ReadValue rv = new ReadValue();

	public void hentOppskrift(String navn) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");

			String query = "SELECT inavn, mengde, pnavn, beskrivelse FROM oppskrift_view WHERE cnavn = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, navn);
			ResultSet rs = ps.executeQuery();
			System.out.println("__________________________________________");
			System.out.format("\n%20s\n", navn);

			String cu = "0";
			String ne = "1";
			while (rs.next()){
				ne = rs.getString(1);
				// --- Printer bare navnet paa ingrediensen én gang
				if(!cu.equals(ne)){
					System.out.format("\n%15s%10s", rs.getString(1), rs.getString(2));
					if (rs.getString(3) != null){
						System.out.print(" | " + rs.getString(3));
					}

				} else{
					System.out.print(" / " + rs.getString(3));
				}
				cu  = ne;
				if (rs.isLast()){
					System.out.println("\n");
					System.out.println(rs.getString(4));
				}
			}
		} catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void leggTilIngrediens(String ingrediens, String type){
		try {
			Class.forName("org.postgresql.Driver");

			// Add type hvis den ikke finnes allerede
			String intype = "INSERT INTO ingredienstype (itnavn) "  +
						"SELECT ? WHERE NOT EXISTS (SELECT * FROM ingredienstype WHERE itnavn = ?)";

			// Add ingrediens hvis den ikke finnes allerede
            String ingred = "INSERT INTO ingredienser (inavn, itid) " +
						"SELECT ?, (SELECT itid FROM ingredienstype where itnavn = ?) " +
						"WHERE NOT EXISTS (SELECT * FROM ingredienser where inavn = ?)";

            PreparedStatement ps1 = connection.prepareStatement(intype);
            PreparedStatement ps2 = connection.prepareStatement(ingred);

			ps1.setString(1, type);
			ps1.setString(2, type);
			ps2.setString(1, ingrediens);
			ps2.setString(2, type);
			ps2.setString(3, ingrediens);
			ps1.executeUpdate();
            ps2.executeUpdate();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}


	public void leggTilDrink(String navn, String beskrivelse){
		try {
			Class.forName("org.postgresql.Driver");

			// Add ingrediens hvis den ikke finnes allerede
            String query = "INSERT INTO cocktails (cnavn, beskrivelse) " +
						"SELECT ?, ? WHERE NOT EXISTS (SELECT * FROM cocktails WHERE cnavn = ?)";

            PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, navn);
			ps.setString(2, beskrivelse);
			ps.setString(3, navn);
            ps.executeUpdate();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void leggTilMaaleenhet(String enhet){
		try {
			Class.forName("org.postgresql.Driver");

			// Add ingrediens hvis den ikke finnes allerede
            String query = "INSERT INTO maaleenhet (menavn) " +
						"SELECT ? WHERE NOT EXISTS (SELECT * FROM maaleenhet WHERE menavn = ?)";

			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, enhet);
			ps.setString(2, enhet);
            ps.executeUpdate();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}


	public void leggTilProdukt(String produkt, String type, String ingrediens){
		try {
			Class.forName("org.postgresql.Driver");

			// Add ingrediens hvis den ikke finnes allerede
            String typNavn = "INSERT INTO ingredienstype (itnavn) " +
						"SELECT ? WHERE NOT EXISTS (SELECT * FROM ingredienstype WHERE itnavn = ?)";
			// Add ingrediens hvis den ikke finnes allerede
            String ingNavn = "INSERT INTO ingredienser (inavn, itid) " +
						"SELECT ?, (SELECT itid FROM ingredienstype WHERE itnavn = ?) " +
						"WHERE NOT EXISTS (SELECT * FROM ingredienser WHERE inavn = ?)";
			// Add produkt hvis den ikke finnes allerede
            String proNavn = "INSERT INTO produkt (pnavn, iid) " +
						"SELECT ?, (SELECT iid FROM ingredienser WHERE inavn = ?) " +
						"WHERE NOT EXISTS (SELECT * FROM produkt WHERE pnavn = ?)";

			PreparedStatement ps1 = connection.prepareStatement(typNavn);
			PreparedStatement ps2 = connection.prepareStatement(ingNavn);
			PreparedStatement ps3 = connection.prepareStatement(proNavn);

			ps1.setString(1, type);
			ps1.setString(2, type);

			ps2.setString(1, ingrediens);
			ps2.setString(2, type);
			ps2.setString(3, ingrediens);

			ps3.setString(1, produkt);
			ps3.setString(2, ingrediens);
			ps3.setString(3, produkt);

            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void leggTilAnbefaling(String drink, String ingrediens, String type, String produkt){

		try {
			Class.forName("org.postgresql.Driver");

			// Add produkt hvis den ikke finnes allerede
			leggTilProdukt(produkt, type, ingrediens);

			// Add anbefaling hvis den ikke finnes allerede
            String anbef = "INSERT INTO anbefaling (cid, pid) " +
							"SELECT (SELECT cid FROM cocktails WHERE cnavn = ?), " +
							"(SELECT pid FROM produkt WHERE pnavn = ?) " +
							"WHERE NOT EXISTS (SELECT * FROM anbefaling WHERE cid = " +
							"(SELECT cid FROM cocktails WHERE cnavn = ?) " +
							"AND pid = (SELECT pid FROM produkt WHERE pnavn = ?))";

			PreparedStatement ps = connection.prepareStatement(anbef);

			ps.setString(1, drink);
			ps.setString(2, produkt);
			ps.setString(3, drink);
			ps.setString(4, produkt);

            ps.executeUpdate();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}


	public void leggTilOppskrift(String drink, String ingrediens, String mengde, String maaleenhet){

		try {
			Class.forName("org.postgresql.Driver");
			String query = "INSERT INTO oppskrift (cid, iid, mengde, meid) " +
						"SELECT (SELECT cid FROM cocktails WHERE cnavn = ?), " +
						"(SELECT iid FROM ingredienser WHERE inavn = ?), " +
						"?, " + // mengde
						"(SELECT meid FROM maaleenhet WHERE menavn = ?) " +
						"WHERE NOT EXISTS (SELECT * FROM oppskrift WHERE cid = " +
						"(SELECT cid FROM cocktails WHERE cnavn = ?) " +
						"AND iid = (SELECT iid FROM ingredienser WHERE inavn = ?))";

			PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, drink);
			ps.setString(2, ingrediens);
			ps.setString(3, mengde);
			ps.setString(4, maaleenhet);
			ps.setString(5, drink);
			ps.setString(6, ingrediens);

			System.out.println(ps.executeUpdate());

			// Add anbefaling hvis den ikke finnes allerede

		}catch(SQLException|ClassNotFoundException ex){
			System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void begin(){
		try {
			Class.forName("org.postgresql.Driver");
			connection.createStatement().executeUpdate("BEGIN;");
		}catch(SQLException|ClassNotFoundException ex){
			System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void commit(){
		try {
			Class.forName("org.postgresql.Driver");
			connection.createStatement().executeUpdate("COMMIT;");
		}catch(SQLException|ClassNotFoundException ex){
			System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void rollback(){
		try {
			Class.forName("org.postgresql.Driver");
			connection.createStatement().executeUpdate("ROLLBACK;");
		}catch(SQLException|ClassNotFoundException ex){
			System.err.println("Error encountered: " + ex.getMessage());
		}
	}
}
