package Data.daos;

import Data.intefaces.StatisticDAO;
import Data.model.Category;
import Data.model.Statistic;
import core.Executer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAOImpl implements StatisticDAO {

    private Executer exec;

    @Override
    public List<Statistic> getAllStatistics() {
        ArrayList<Statistic> statistics = new ArrayList<>();
        ResultSet results = exec.executeQuery("SELECT * FROM Statistics");

        //populating the List
        try {
            while (results.next()) {
                int number_of_misses = results.getInt("number_of_misses");
                int id = results.getInt("statisticId");
                //TODO Add foreign word table and player table
                Statistic temp = new Statistic(id, number_of_misses);
                statistics.add(temp);
                System.out.println(id + " " + number_of_misses);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }

    @Override
    public Statistic getStatistic(int id) {
        ResultSet result = exec.executeQuery("SELECT * FROM Statistics WHERE statisticId = '" + id + "'");
        try {
            result.next();
            int resultNumberOfMisses = result.getInt("number_of_misses");
            int resultId = result.getInt("statisticId");
            //TODO Add foreign keys;
            Statistic temp = new Statistic(resultId, resultNumberOfMisses);
            return temp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addStatistic(Statistic statistic) {
        exec.executeUpdate("INSERT INTO Statistics VALUES ('" + statistic.getNumberOfMisses() + "')");
        System.out.println(statistic.getStatisticId() + " Added to Database");
    }

    @Override
    public void updateStatistic(Statistic statistic) {
        exec.executeUpdate("UPDATE Statistics SET content = '" + statistic.getNumberOfMisses() + "' WHERE id= " + statistic.getStatisticId());
        this.getStatistic(statistic.getStatisticId());
    }

    @Override
    public void deleteStatistic(Statistic statistic) {
        exec.executeUpdate("DELETE FROM Statistics WHERE id='" + statistic.getStatisticId() + "'");
    }
}
