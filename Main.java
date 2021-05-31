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

	public static void main(String[] args){

		rv.hentAlleOppskrifter();
		//alleDrinks();
		//alleIngredienser();
	}

    public static void alleDrinks() {
		rv.hentAlleDrinks();
    }

    public static void alleIngredienser() {
		rv.hentAlleIngredienser();
    }
}
