import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBConnection {
    private String user = "eriknystad";
    private String pwd = "Password123";
    private String host = "jdbc:postgresql://localhost:";
    private String port = "5432";
    private String databse = "/postgres";

    public Connection getConnection() {
        Connection connection = null;
        try {
            // Load driver for PostgreSQL
            Class.forName("org.postgresql.Driver");
            // Create a connection to the database
            connection = DriverManager.getConnection(host + port + databse, user, pwd);

            if (connection != null) {
                //System.out.println("Connection successful!");
                return connection;
            } else {
                System.out.println("Connection not successful!");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Error encountered: " + ex.getMessage());
        }
        return null;
    }
}
