package communication.messages;

public class PlayerJoinedNotification implements Message {
    String messageType = "PlayerJoinedNotification";
    String newPlayerName;
    int numPlayers;

    public PlayerJoinedNotification(String newPlayerName, int numPlayers) {
        this.newPlayerName = newPlayerName;
        this.numPlayers = numPlayers;
    }

    public String getNewPlayerName() {
        return newPlayerName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNewPlayerName(String newPlayerName) {
        this.newPlayerName = newPlayerName;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
