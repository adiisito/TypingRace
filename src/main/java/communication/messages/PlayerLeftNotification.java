package communication.messages;

public class PlayerLeftNotification {
    String messageType = "PlayerLeftNotification";
    String playerName;
    int numPlayers;

    public PlayerLeftNotification(String playerName, int numPlayers) {
        this.playerName = playerName;
        this.numPlayers = numPlayers;
    }
    public PlayerLeftNotification(String playerName) {
        this.playerName = playerName;
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

    public String getMessageType() {
        return messageType;
    }
}
