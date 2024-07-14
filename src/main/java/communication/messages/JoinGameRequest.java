package communication.messages;

/**
 * Represents a request to join a game, sent from a client to the game server.
 * This class contains all necessary details needed to process a player's request to join a game,
 * specifically the player's name.
 */
public class JoinGameRequest {

    /**
     * The message type identifier for this request.
     */
    private final String messageType = "JoinGameRequest";

    private String playerName;

    /**
     * Constructs a new join game request with the player's name.
     *
     * @param playerName The name of the player requesting to join the game.
     */
    public JoinGameRequest(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Retrieves the type of message, indicating this is a join game request.
     *
     * @return The message type as a string.
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Gets the name of the player who wants to join the game.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets or updates the player's name in the join game request.
     * This can be used to correct the player's name if it was entered incorrectly at the initial request.
     *
     * @param playerName The new name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
