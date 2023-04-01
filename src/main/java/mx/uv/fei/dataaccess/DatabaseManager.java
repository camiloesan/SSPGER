package mx.uv.fei.dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatabaseManager {
    private Connection connection;
    private final String DATABASE_NAME = "jdbc:mariadb://localhost/congreso";
    private final String DATABASE_USER = "usuario";
    private final String DATABASE_PASSWORD = "roschester";

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD);
    }

    public Connection getConnection() throws SQLException {
        connect();
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException exception) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, "error", exception);
            }
        }
    }    
}
