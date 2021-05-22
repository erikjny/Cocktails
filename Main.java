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


	public static void main(String[] args){
		allIngredients();
	}

    public static void allIngredients() {
        // Get connection from DBConnection class
        DBConnection dbc = new DBConnection();
        connection = dbc.getConnection();

        try {
            // Load driver for PostgreSQL
            Class.forName("org.postgresql.Driver");

            query = "Select* From cocktails";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                System.out.println("sid: " + rs.getString(1));
                System.out.println("name: " + rs.getString(2));
            }

        } catch (SQLException|ClassNotFoundException ex) {
            System.err.println("Error encountered: " + ex.getMessage());
        }

    }
}
