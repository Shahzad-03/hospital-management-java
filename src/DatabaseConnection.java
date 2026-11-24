// DatabaseConnection.java - Handles DB connection
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/hospital";
    private static final String USER = "root"; // Change as needed
    private static final String PASSWORD = ""; // Change as needed

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MariaDB driver not found", e);
        }
    }
}