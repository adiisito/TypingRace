package communication.messages;

/** The type Game end notification. */
public class GameEndNotification {
  String messageType = "GameEndNotification";
  String playerName;
  int wpm;
  long time;
  double accuracy;

  /**
   * Instantiates a new Game end notification.
   *
   * @param playerName the player name
   * @param wpm the wpm
   * @param accuracy the accuracy
   * @param time the time
   */
  public GameEndNotification(String playerName, int wpm, double accuracy, long time) {
    this.playerName = playerName;
    this.wpm = wpm;
    this.accuracy = accuracy;
    this.time = time;
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
  public long getTime() {
    return time;
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
