package game;

/**
 * The interface Player.
 */
public interface Player {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets wpm.
     *
     * @return the wpm
     */
    int getWpm();

    /**
     * Sets wpm.
     *
     * @param wpm the wpm
     */
    void setWpm(int wpm);

    /**
     * Gets progress.
     *
     * @return the progress
     */
    int getProgress();

    /**
     * Sets progress.
     *
     * @param progress the progress
     */
    void setProgress(int progress);

    /**
     * Receive progress update.
     *
     * @param playerName the player name
     * @param progress   the progress
     * @param wpm        the wpm
     */
    void receiveProgressUpdate(String playerName, int progress, int wpm);

    /**
     * Gets accuracy.
     *
     * @param accuracy the accuracy
     */
// String getId();
    void setAccuracy(double accuracy);

    /**
     * Gets accuracy.
     *
     * @return the accuracy
     */
    double getAccuracy();

    /**
     * Sets has finished.
     *
     * @param hasFinished the has finished
     */
    void setHasFinished(boolean hasFinished);

    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    boolean isCompleted();
}

