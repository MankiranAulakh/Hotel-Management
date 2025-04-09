package hotelManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://44.211.45.143:3306/testdb";
    private static final String USER = "newuser";
    private static final String PASSWORD = "password";

    // Step 1: Private constructor
    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to database.");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
    }

    // Step 2: Public static method to get the singleton instance
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Step 3: Public method to get the JDBC connection
    public Connection getConnection() {
        return connection;
    }
}
