package communication.messages;

/**
 * Represents a request indicating that a player has left the game.
 * This message is used to inform the server and possibly other clients that a player has
 * discontinued their participation in the current game session.
 */
public class PlayerLeftRequest {

    /**
     * Constant identifier for this type of message.
     */
    private final String messageType = "PlayerLeftRequest";

    private String playerName;

    /**
     * Creates a new request to notify that a player has left the game.
     *
     * @param playerName The name of the player who has left.
     */
    public PlayerLeftRequest(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Retrieves the message type of this request, which is always 'PlayerLeftRequest'.
     *
     * @return The message type as a string.
     */
    public String getMessage() {
        return messageType;
    }

    /**
     * Gets the name of the player who has left the game.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets or updates the name of the player who has left the game.
     * This might be used to correct or update the player’s name if necessary before sending the request.
     *
     * @param playerName The new name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
