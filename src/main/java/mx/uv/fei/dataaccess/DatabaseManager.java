package mx.uv.fei.dataaccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatabaseManager {
    private Connection connection;
    private static String DATABASE_NAME;
    private static String DATABASE_USER;
    private static String DATABASE_PASSWORD;
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DatabaseManager.class);

    public DatabaseManager() {
        String configFilePath = "src/main/java/mx/uv/fei/config.properties";
        FileInputStream propertiesInput;
        try {
            propertiesInput = new FileInputStream(configFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Properties prop = new Properties();
        try {
            prop.load(propertiesInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DATABASE_NAME = (String) prop.get("DATABASE_NAME");
        DATABASE_USER = (String) prop.get("DATABASE_USER");
        DATABASE_PASSWORD = (String) prop.get("DATABASE_PASSWORD");
    }

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