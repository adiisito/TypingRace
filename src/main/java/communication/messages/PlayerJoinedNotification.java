// PlayerJoinedNotification Class
package communication.messages;

/**
 * The type Player joined notification.
 */
public class PlayerJoinedNotification {
    private String messageType = "PlayerJoinedNotification";
    private String playerName;
    private int numPlayers;

    /**
     * Instantiates a new Player joined notification.
     *
     * @param playerName the player name
     * @param numPlayers the num players
     */
    public PlayerJoinedNotification(String playerName, int numPlayers) {
        this.playerName = playerName;
        this.numPlayers = numPlayers;
    }

    /**
     * Gets message type.
     *
     * @return the message type
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets num players.
     *
     * @return the num players
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
