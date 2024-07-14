package game;

import java.util.List;

/** The interface Game. */
public interface Game {
  /** Start race. */
  void startRace();

  /**
   * Update player progress.
   *
   * @param player the player
   * @param progress the progress
   */
  void updatePlayerProgress(Player player, int progress);

  /**
   * Gets results.
   *
   * @return the results
   */
  List<Player> getResults();

  /** End race. */
  void endRace();
}
