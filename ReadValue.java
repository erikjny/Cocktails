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

			System.out.format("\n%20s\n", navn);

			String cu = "0";
			String ne = "1";
			while (rs.next()){
				ne = rs.getString(1);
				// --- Printer bare navnet paa ingrediensen én gang
				if(!cu.equals(ne)){
					System.out.format("%15s%10s", rs.getString(1), rs.getString(2));
					if (rs.getString(3) != null){
						System.out.print(" | " + rs.getString(3));
					} else{
						System.out.println("");
					}
				} else{
					System.out.println(" / " + rs.getString(3));
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
}
