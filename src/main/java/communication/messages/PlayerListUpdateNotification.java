package communication.messages;

import java.util.List;

/** The type Player list update notification. */
public class PlayerListUpdateNotification {
  private final String messageType = "PlayerListUpdateNotification";
  private final List<String> playerNames;

  /**
   * Instantiates a new Player list update notification.
   *
   * @param playerNames the player names
   */
  public PlayerListUpdateNotification(List<String> playerNames) {
    this.playerNames = playerNames;
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
   * Gets player names.
   *
   * @return the player names
   */
  public List<String> getPlayerNames() {
    return playerNames;
  }
}
