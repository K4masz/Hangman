package core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by K4masz on 2018-01-18.
 */
public class Executer {

    private DBConnection conn;
    public Executer(DBConnection conn) {
        this.conn = conn;
    }

    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement p = conn.getConnection().prepareStatement(query);
            ResultSet result = p.executeQuery();

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void executeUpdate(String query) {
        try (PreparedStatement p = conn.getConnection().prepareStatement(query)) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
