package app.utils;

import com.mysql.jdbc.Connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class ConnectionManager {
    private static String driverName = "com.mysql.jdbc.Driver";
    private String user;
    private String password;
    private String dblink;
    private static Connection con;
    private static ConnectionManager instance = null;

    ConnectionManager(String dblink, String user, String password) {
        this.dblink = dblink;
        this.user = user;
        this.password = password;
    }

    protected static HashMap<String, String> getConfiguration() {
        Properties prop = new Properties();
        InputStream input = null;
        HashMap<String, String> configuration = new HashMap<>();

        try {
            input = new FileInputStream("src/config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            configuration.put("dblink", prop.getProperty("dblink"));
            configuration.put("user", prop.getProperty("dbuser"));
            configuration.put("password", prop.getProperty("dbpassword"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return configuration;
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            HashMap<String, String> configuration = ConnectionManager.getConfiguration();
            instance = new ConnectionManager(
                configuration.get("dblink"),
                configuration.get("user"),
                configuration.get("password")
            );
        }

        return instance;
    }

    public Connection getConnection() {
        try {
            Class.forName(driverName);

            try {
                con = (Connection) DriverManager.getConnection(this.dblink, this.user, this.password);
            } catch (SQLException e) {
                // log an exception
                Screen.exception(e, "Failed to create the database connection.");
            }
        } catch (ClassNotFoundException e) {
            // log an exception
            Screen.exception(e, "Driver not found.");
        }

        return con;
    }
}
