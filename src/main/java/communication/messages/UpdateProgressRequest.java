package communication.messages;

/**
 * Represents a request to update a player's progress metrics in a game.
 * This class is used to send details about a player's performance, such as typing speed, accuracy, and overall progress.
 */
public class UpdateProgressRequest {

    /**
     * Message type identifier for this request.
     */
    private final String messageType = "UpdateProgressRequest";

    private String playerName;
    private int wpm;
    private int time;
    private int progress;
    private double accuracy;

    /**
     * Constructs a new request to update a player's game progress.
     *
     * @param playerName The name of the player.
     * @param wpm Words per minute typed by the player.
     * @param progress Current completion percentage of the task or game.
     * @param accuracy Typing accuracy percentage.
     * @param time Time elapsed during the current game or task.
     */
    public UpdateProgressRequest(String playerName, int wpm, int progress, double accuracy, int time) {
        this.playerName = playerName;
        this.wpm = wpm;
        this.progress = progress;
        this.accuracy = accuracy;
        this.time = time;
    }

    /**
     * Gets the type of message for this request.
     *
     * @return The message type as a string.
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Gets the player's name.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player's name.
     *
     * @param playerName The new name of the player.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the words per minute.
     *
     * @return Number of words typed per minute.
     */
    public int getWpm() {
        return wpm;
    }

    /**
     * Sets the words per minute.
     *
     * @param wpm Number of words to set per minute.
     */
    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    /**
     * Gets the elapsed time.
     *
     * @return Time in seconds.
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the elapsed time.
     *
     * @param time Time in seconds.
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Gets the current progress.
     *
     * @return Progress as a percentage.
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the current progress.
     *
     * @param progress Progress percentage to set.
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Gets the accuracy of the player.
     *
     * @return Accuracy as a percentage.
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the accuracy of the player.
     *
     * @param accuracy Accuracy percentage to set.
     */
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
