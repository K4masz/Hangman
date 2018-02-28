package Data.daos;

import Data.intefaces.PlayerDAO;
import Data.model.Player;
import core.DBConnection;
import core.Executer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAOImpl implements PlayerDAO {

    private Executer exec;

    public PlayerDAOImpl() {
        this.exec = new Executer(DBConnection.getInstance());

    }

    @Override
    public List<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        ResultSet results = exec.executeQuery("SELECT * FROM Players");

        //populating the List
        try {
            while (results.next()) {
                String name = results.getString("name");
                int id = results.getInt("playerId");
                Player temp = new Player(id, name);
                players.add(temp);
                System.out.println(id + " " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    @Override
    public Player getPlayer(int id) {
        ResultSet result = exec.executeQuery("SELECT * FROM Players WHERE id = '" + id);
        try {
            result.next();
            String resultName = result.getString("name");
            int resultId = result.getInt("id");
            Player temp = new Player(resultId, resultName);
            return temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addPlayer(Player player) {
        exec.executeUpdate("INSERT INTO Players VALUES (1 , '" + player.getName() + "')");
        System.out.println(player.getName() + " Added to Database");
    }

    @Override
    public void updatePlayer(Player player) {
        exec.executeUpdate("UPDATE Players SET name = '" + player.getName() + "' WHERE id= " + player.getplayerId());
        this.getPlayer(player.getplayerId());
    }

    @Override
    public void deletePlayer(Player player) {
        exec.executeUpdate("DELETE FROM Player WHERE id='" + player.getplayerId() + "'");
    }
}
