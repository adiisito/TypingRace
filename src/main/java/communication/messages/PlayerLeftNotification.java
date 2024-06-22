package communication.messages;

public class PlayerLeftNotification implements Message {
    String messageType = "PlayerLeftNotification";
    String playerName;
    int numPlayers;

    public PlayerLeftNotification(String playerName, int numPlayers) {
        this.playerName = playerName;
        this.numPlayers = numPlayers;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
