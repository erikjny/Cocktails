import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DBConnection {
    private String user = "postgres";
    private String pwd = "Password123";
    private String host = "jdbc:postgresql://localhost:";
    private String port = "5433";
    private String databse = "/drinks";

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
