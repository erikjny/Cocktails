import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadValue{
    private String query;
    private Statement statement;
    private PreparedStatement ps;
    private ResultSet rs;
	private DBConnection dbc = new DBConnection();
	private Connection connection = dbc.getConnection();

	public void hentAlleDrinks(){
		try {
			Class.forName("org.postgresql.Driver");

            query = "Select * From cocktails";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

			while (rs.next()){
                System.out.print("sid: " + rs.getString(1) + "	");
                System.out.println("name: " + rs.getString(2));
                System.out.println("	" + rs.getString(3) + "\n");
			}
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void hentAlleIngredienser(){
		try {
			Class.forName("org.postgresql.Driver");

            query = "Select inavn, itnavn FROM ingredienser NATURAL JOIN ingredienstype";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

			while (rs.next()){
                System.out.print("" + rs.getString(1) + "	");
                System.out.println("| " + rs.getString(2));
			}
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void hentOppskrift(String navn) throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT inavn, mengde, pnavn, beskrivelse FROM oppskrift_view WHERE cnavn = ?";
			ps = connection.prepareStatement(query);
			ps.setString(1, navn);
			ResultSet rs = ps.executeQuery();

			System.out.println("__________________________________________");
			System.out.format("\n%23s\n", navn);

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
		} finally {
			ps.close();
		}
	}

	public void hentAlleOppskrifter(){
		try {
			Class.forName("org.postgresql.Driver");

			DBConnection dbc2 = new DBConnection();
			Connection connection2 = dbc2.getConnection();

			query = "SELECT DISTINCT cnavn FROM oppskrift_view";
            statement = connection2.createStatement();
            rs = statement.executeQuery(query);

			while (rs.next()){
				hentOppskrift(rs.getString(1));
			}
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	// <<<< --------- SJEKK OM VERDIER FINNES --------- >>>>

	public boolean drinkFinnes(String drink){
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT 1 FROM cocktails WHERE cnavn = ?";
            PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, drink);
            rs = ps.executeQuery();

			return rs.next();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
			return false;
		}
	}

	public boolean maaleenhetFinnes(String maaleenhet){
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT 1 FROM maaleenhet WHERE menavn = ?";
            PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, maaleenhet);
            rs = ps.executeQuery();

			return rs.next();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
			return false;
		}
	}

	public boolean ingredienstypeFinnes(String ingredienstype){
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT 1 FROM ingredienstype WHERE itnavn = ?";
            PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, ingredienstype);
            rs = ps.executeQuery();

			return rs.next();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
			return false;
		}
	}

	public boolean produktFinnes(String produkt){
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT 1 FROM produkt WHERE pnavn = ?";
            PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, produkt);
            rs = ps.executeQuery();

			return rs.next();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
			return false;
		}
	}

	public boolean ingrediensFinnes(String ingrediens){
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT 1 FROM ingredienser WHERE inavn = ?";
            PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, ingrediens);
            rs = ps.executeQuery();

			return rs.next();
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
			return false;
		}
	}

	public void drinkFraIngredienser(ArrayList<String> ingredienser){
		try {
			Class.forName("org.postgresql.Driver");

			DBConnection dbc2 = new DBConnection();
			Connection con = dbc2.getConnection();

			String query = "select cnavn from cocktails c " +
					"WHERE NOT EXISTS (select 1 from oppskrift o " +
                    "WHERE o.cid = c.cid " +
                      " and o.iid not in (";

			for(int i = 0; i < ingredienser.size(); i++){
				query += "(SELECT iid FROM ingredienser WHERE inavn = '" + ingredienser.get(i) + "')";
				if (i < ingredienser.size()-1){
					query += ", ";
				}
			}

			query += "))";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);

			while(rs.next()){
				hentOppskrift(rs.getString(1));
			}

		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

	public void enManglende(ArrayList<String> ingredienser){
		try {
			Class.forName("org.postgresql.Driver");

			DBConnection dbc2 = new DBConnection();
			Connection con = dbc2.getConnection();
			String query = "SELECT cnavn FROM cocktails c " +
							"natural join oppskrift " +
							"WHERE iid IN (";

			// Legger til ingrediensene i spørringen
			for(int i = 0; i < ingredienser.size(); i++){
				query += "(SELECT iid FROM ingredienser WHERE inavn = '" + ingredienser.get(i) + "')";
				if (i < ingredienser.size()-1){
					query += ", ";
				}
			}
			query += ") ";
			query   += 	"GROUP BY cnavn " +
							"HAVING count(*) " +
							"= (SELECT count(*)-1 FROM oppskrift " +
							"NATURAL JOIN  cocktails "+
							"where cnavn = c.cnavn)";

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);

			while(rs.next()){
				hentOppskrift(rs.getString(1));
			}

		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}
}
