package communication.messages;

/** The type Player left notification. */
public class PlayerLeftNotification {
  String messageType = "PlayerLeftNotification";
  String playerName;
  int numPlayers;

  /**
   * Instantiates a new Player left notification.
   *
   * @param playerName the player name
   * @param numPlayers the num players
   */
  public PlayerLeftNotification(String playerName, int numPlayers) {
    this.playerName = playerName;
    this.numPlayers = numPlayers;
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
   * Gets num players.
   *
   * @return the num players
   */
  public int getNumPlayers() {
    return numPlayers;
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
