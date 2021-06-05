import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WriteValue{
    private String intype;
    private String ingred;
    private Statement statement;
    private PreparedStatement ps1;
    private PreparedStatement ps2;
    private ResultSet rs;
	private DBConnection dbc = new DBConnection();
	private Connection connection = dbc.getConnection();

	public void leggTilIngrediens(String ingrediens, String type){
		try {
			// Add type hvis den ikke finnes allerede
			intype = "INSERT INTO ingredienstype (itnavn) "  +
						"SELECT ? WHERE NOT EXISTS (SELECT * FROM ingredienstype WHERE itnavn = ?)";

			// Add ingrediens hvis den ikke finnes allerede
            ingred = "INSERT INTO ingredienser (inavn, itid) " +
						"SELECT ?, (SELECT itid FROM ingredienstype where itnavn = ?) " +
						"WHERE NOT EXISTS (SELECT * FROM ingredienser where inavn = ?)";

            ps1 = connection.prepareStatement(intype);
            ps2 = connection.prepareStatement(ingred);

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

}
