package communication.messages;

import game.TypingPlayer;
import java.util.List;

/**
 * Handles the sending of updated player rankings. This class encapsulates a list of {@link
 * TypingPlayer} instances to update rankings in a gaming environment.
 */
public class UpdateRankingRequest {

  /** Identifier for this type of message. */
  private final String messageType = "UpdateRankingRequest";

  /** List of players to be ranked. */
  private List<TypingPlayer> players;

  /**
   * Initializes a new request with specified player rankings.
   *
   * @param players List of {@link TypingPlayer} for ranking updates.
   */
  public UpdateRankingRequest(List<TypingPlayer> players) {
    this.players = players;
  }

  /**
   * Retrieves the current list of players.
   *
   * @return List of {@link TypingPlayer}.
   */
  public List<TypingPlayer> getPlayers() {
    return players;
  }

  /**
   * Updates the list of players to be ranked.
   *
   * @param players New list of {@link TypingPlayer}.
   */
  public void setPlayers(List<TypingPlayer> players) {
    this.players = players;
  }
}
