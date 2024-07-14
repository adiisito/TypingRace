package communication.messages;

/**
 * Represents a notification message indicating that a lobby is full. This class is typically used
 * in the context of multiplayer games or applications where users join lobbies, and it is necessary
 * to inform when no more users can join due to capacity limits.
 */
public class LobbyFullNotification {

  /** The type of message being sent, indicating that the lobby is full. */
  private final String messageType = "LobbyFullNotification";

  /**
   * Retrieves the type of message. This method returns a constant value that identifies the message
   * as a lobby full notification, which can be used by message handling systems to route or process
   * the message correctly.
   *
   * @return the message type as a String
   */
  public String getMessageType() {
    return messageType;
  }
}
