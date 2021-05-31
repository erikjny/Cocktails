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

            query = "SELECT cnavn, inavn, mengde, menavn, beskrivelse FROM oppskrift " +
													"NATURAL JOIN cocktails " +
													"NATURAL JOIN ingredienser " +
													"NATURAL JOIN maaleenhet";

            statement = connection.createStatement();
            rs = statement.executeQuery(query);

			String curr = "";
			String next = "1";
			while (rs.next()){

				next = rs.getString(1);
				if (!curr.equals(next)){
					System.out.println("\n==== " + rs.getString(1) + "====");
					System.out.println(rs.getString(5) + "\n");
				}
                System.out.print(rs.getString(2));
                System.out.print(" | " + rs.getString(3));
                System.out.println(rs.getString(4));

				curr = next;
			}
		}catch(SQLException|ClassNotFoundException ex){
            System.err.println("Error encountered: " + ex.getMessage());
		}
	}

}
