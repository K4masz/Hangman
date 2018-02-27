package core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by K4masz on 2018-01-18.
 */
public class DBConnection {

    private static DBConnection inst;

    private Connection connection;
    private String DB_URL = "jdbc:oracle:thin:@155.158.112.45:1521:oltpstud";
    private String USER = "ziibd6";
    private String PASS = "trevor12";

    private DBConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection established.");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (inst == null)
            return new DBConnection();
        else
            return inst;
    }

    public Connection getConnection() {
        return connection;
    }
}