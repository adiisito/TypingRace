package communication.messages;

import game.TypingPlayer;
import java.util.List;

/**
 * Represents a notification containing the rankings of players in a typing game. This class
 * encapsulates a list of {@link TypingPlayer} objects, each representing a player's performance and
 * rank.
 *
 * <p>The {@code RankingNotification} class is designed to be used in environments where game
 * results are communicated, particularly in multi-player scenarios where rankings are compiled and
 * need to be displayed or processed.
 */
public class RankingNotification {

  /** The message type identifier for this notification. */
  private final String messageType = "RankingNotification";

  /** List of players and their corresponding rankings. */
  private List<TypingPlayer> rankings;

  /**
   * Constructs a new {@code RankingNotification} with the specified list of players' rankings.
   *
   * @param rankings the list of {@link TypingPlayer} objects representing the players and their
   *     ranks.
   */
  public RankingNotification(List<TypingPlayer> rankings) {
    this.rankings = rankings;
  }

  /**
   * Retrieves the list of player rankings.
   *
   * @return a {@link List} of {@link TypingPlayer} objects representing the rankings of the
   *     players.
   */
  public List<TypingPlayer> getRankings() {
    return rankings;
  }

  /**
   * Updates the list of player rankings with a new list of players.
   *
   * @param rankings a {@link List} of {@link TypingPlayer} objects representing the new rankings of
   *     the players.
   */
  public void setRankings(List<TypingPlayer> rankings) {
    this.rankings = rankings;
  }
}
