package Data.model;

public class Player {

    private int playerId;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public Player(int playerId, String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public Player() {
    }

    public int getplayerId() {
        return playerId;
    }

    public void setplayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
