package communication.messages;

import game.TypingPlayer;
import java.util.List;

/** The type Game start notification. */
public class GameStartNotification {

  private final String messageType = "GameStartNotification";
  private final List<TypingPlayer> players;
  private final String text;

  /**
   * Instantiates a new Game start notification.
   *
   * @param players the players
   * @param text the text
   */
  public GameStartNotification(List<TypingPlayer> players, String text) {
    this.players = players;
    this.text = text;
  }

  /**
   * Gets text.
   *
   * @return the text
   */
  public String getText() {
    return text;
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
   * Gets players.
   *
   * @return the players
   */
  public List<TypingPlayer> getPlayers() {
    return players;
  }

  /**
   * Gets num players.
   *
   * @return the num players
   */
  public int getNumPlayers() {
    return players.size();
  }
}
