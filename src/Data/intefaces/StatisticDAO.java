package Data.intefaces;

import Data.model.Statistic;

import java.util.List;

public interface StatisticDAO {
    List<Statistic> getAllStatistics();
    Statistic getStatistic(int id);
    void addStatistic(Statistic statistic);
    void updateStatistic(Statistic statistic);
    void deleteStatistic(Statistic statistic);
}
