package communication.messages;

/**
 * Represents a request to conclude a game session, sent from the client to the server.
 * This class encapsulates the final game statistics for a player, including their name,
 * time spent, words per minute (wpm), and accuracy percentage.
 */
public class EndGameRequest {

    /**
     * The message type identifier for this request.
     */
    private final String messageType = "EndGameRequest";

    private String playerName;
    private long time;
    private int wpm;
    private double accuracy;

    /**
     * Constructs a new EndGameRequest with the specified player's game-ending statistics.
     *
     * @param playerName The name of the player.
     * @param time The total time taken by the player to finish the game, in milliseconds.
     * @param wpm The words per minute typed by the player.
     * @param accuracy The typing accuracy of the player as a percentage.
     */
    public EndGameRequest(String playerName, long time, int wpm, double accuracy) {
        this.playerName = playerName;
        this.time = time;
        this.wpm = wpm;
        this.accuracy = accuracy;
    }

    /**
     * Returns the typing accuracy of the player.
     *
     * @return The accuracy percentage.
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the typing accuracy of the player.
     *
     * @param accuracy The new accuracy percentage.
     */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Retrieves the type of message, indicating this is an end game request.
     *
     * @return The message type as a string.
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Gets the name of the player associated with this request.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets or updates the name of the player for this request.
     *
     * @param playerName The new name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the time taken by the player to finish the game.
     *
     * @return The time in milliseconds.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time taken by the player to finish the game.
     *
     * @param time The new time in milliseconds.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Gets the words per minute rate achieved by the player.
     *
     * @return The words per minute rate.
     */
    public int getWpm() {
        return wpm;
    }

    /**
     * Sets the words per minute rate for the player.
     *
     * @param wpm The new words per minute rate.
     */
    public void setWpm(int wpm) {
        this.wpm = wpm;
    }
}
