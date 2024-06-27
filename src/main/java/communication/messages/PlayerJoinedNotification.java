// PlayerJoinedNotification Class
package communication.messages;

public class PlayerJoinedNotification {
    private String messageType = "PlayerJoinedNotification";
    private String playerName;
    private int numPlayers;

    public PlayerJoinedNotification(String playerName, int numPlayers) {
        this.playerName = playerName;
        this.numPlayers = numPlayers;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
