// PlayerListUpdateNotification Class
package communication.messages;

import java.util.List;

public class PlayerListUpdateNotification {
  private String messageType = "PlayerListUpdateNotification";
  private List<String> playerNames;

  public PlayerListUpdateNotification(List<String> playerNames) {
    this.playerNames = playerNames;
  }

  public String getMessageType() {
    return messageType;
  }

  public List<String> getPlayerNames() {
    return playerNames;
  }
}
