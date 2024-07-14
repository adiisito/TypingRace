package communication.messages;

/**
 * Represents a request to start a game session, sent from the client to the server.
 * This class includes the host's player name and a specific text that players need to type,
 * serving as the initial setup for a new game round.
 */
public class StartGameRequest {

    /**
     * The message type identifier for this request.
     */
    private final String messageType = "StartGameRequest";

    private String hostPlayerName;
    private String providedText;

    /**
     * Constructs a new StartGameRequest with the host's player name and the text to be typed by players.
     *
     * @param hostPlayerName The name of the player hosting the game.
     * @param providedText The text that players will type during the game.
     */
    public StartGameRequest(String hostPlayerName, String providedText) {
        this.hostPlayerName = hostPlayerName;
        this.providedText = providedText;
    }

    /**
     * Retrieves the host player's name.
     *
     * @return The name of the host player.
     */
    public String getHostPlayerName() {
        return hostPlayerName;
    }

    /**
     * Sets the host player's name.
     *
     * @param hostPlayerName The new host player's name.
     */
    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
    }

    /**
     * Retrieves the text provided for the game.
     *
     * @return The text to be typed by the players.
     */
    public String getProvidedText() {
        return providedText;
    }

    /**
     * Sets the text to be used in the game.
     *
     * @param providedText The new text for players to type.
     */
    public void setProvidedText(String providedText) {
        this.providedText = providedText;
    }
}
