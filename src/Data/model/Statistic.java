package Data.model;

public class Statistic {

    private int statisticId;
    private int numberOfMisses;


    public Statistic(int statisticId, int numberOfMisses) {
        this.statisticId = statisticId;
        this.numberOfMisses = numberOfMisses;
    }

    public Statistic() {
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        this.statisticId = statisticId;
    }

    public int getNumberOfMisses() {
        return numberOfMisses;
    }

    public void setNumberOfMisses(int numberOfMisses) {
        this.numberOfMisses = numberOfMisses;
    }
}
