import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadValue{
    private String query;
    private Statement statement;
    private PreparedStatement pst;
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

	public void hentAlleOppskrifter(){
		try {
			Class.forName("org.postgresql.Driver");

			query = "SELECT cnavn, inavn, mengde || menavn mengde, pnavn, beskrivelse FROM oppskrift " +
			"natural join cocktails " +
			"natural join ingredienser  " +
			"natural join maaleenhet " +
			"left outer join anbefaling " +
			"natural join produkt ON anbefaling.iid = oppskrift.iid AND anbefaling.cid = oppskrift.cid " +
			"ORDER BY cnavn DESC";


            statement = connection.createStatement();
            rs = statement.executeQuery(query);

			String cu = "0";
			String ne = "1";
			String curr = "0";
			String next = "1";

			while (rs.next()){
				next = rs.getString(1);
				ne = rs.getString(2);

				// --- Printer bare navnet paa drinken én gang
				if (!curr.equals(next)){
					System.out.println("\n==== " + rs.getString(1) + "====");
					System.out.println(rs.getString(5) + "\n");
				}

				// --- Printer bare navnet paa ingrediensen én gang
				if(!cu.equals(ne)){
					System.out.print(rs.getString(2)+  " ");
					System.out.print(rs.getString(3));
					if (rs.getString(4) != null){
						System.out.print(" | " + rs.getString(4));
					} else{
						System.out.println("");
					}
				} else{

						System.out.println(" / " + rs.getString(4));
				}

				cu  = ne;
				curr = next;
			}
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}
}
