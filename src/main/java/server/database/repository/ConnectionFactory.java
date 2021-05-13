package server.database.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private ConnectionFactory(){}

    private final static String URL;

    private final static String USERNAME;

    private final static String PASSWORD;

    static {
        Properties properties = new Properties();
        String propertiesFileName = "src/main/resources/config.properties";

        try{
            properties.load(new FileInputStream(propertiesFileName));
        } catch (IOException exception) {
            exception.printStackTrace();
            try {
                throw new DaoException("File " + propertiesFileName + " not found",exception);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

        URL = properties.getProperty("pg.url");
        USERNAME = properties.getProperty("pg.userName");
        PASSWORD = properties.getProperty("pg.password");
    }

    public static Connection getConnection() throws DaoException {

        Connection connection;
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
            throw new DaoException("Connection is failed", e);
        }

        return connection;
    }
}
