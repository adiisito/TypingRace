package communication.messages;

/** The type Game state notification. */
public class GameStateNotification {
  String messageType = "GameStateNotification";
  String playerName;
  int wpm;
  int time;
  int progress;
  double accuracy;

  /**
   * Instantiates a new Game state notification.
   *
   * @param playerName the player name
   * @param wpm the wpm
   * @param time the time
   * @param progress the progress
   * @param accuracy the accuracy
   */
  public GameStateNotification(
      String playerName, int wpm, int time, int progress, double accuracy) {
    this.playerName = playerName;
    this.wpm = wpm;
    this.time = time;
    this.progress = progress;
    this.accuracy = accuracy;
  }

  /**
   * Gets accuracy.
   *
   * @return the accuracy
   */
  public double getAccuracy() {
    return accuracy;
  }

  /**
   * Sets accuracy.
   *
   * @param accuracy the accuracy
   */
  public void setAccuracy(double accuracy) {
    this.accuracy = accuracy;
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
   * Sets player name.
   *
   * @param playerName the player name
   */
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Gets wpm.
   *
   * @return the wpm
   */
  public int getWpm() {
    return wpm;
  }

  /**
   * Sets wpm.
   *
   * @param wpm the wpm
   */
  public void setWpm(int wpm) {
    this.wpm = wpm;
  }

  /**
   * Gets time.
   *
   * @return the time
   */
  public int getTime() {
    return time;
  }

  /**
   * Gets progress.
   *
   * @return the progress
   */
  public int getProgress() {
    return progress;
  }

  /**
   * Sets progress.
   *
   * @param progress the progress
   */
  public void setProgress(int progress) {
    this.progress = progress;
  }

  /**
   * Gets message type.
   *
   * @return the message type
   */
  public String getMessageType() {
    return messageType;
  }
}
