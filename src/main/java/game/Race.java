package game;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Race.
 */
public class Race {
  private final Map<Player, Integer> playerProgress;

  /**
   * Instantiates a new Race.
   */
  public Race() {
    playerProgress = new HashMap<>();
  }

  /**
   * Update player progress.
   *
   * @param player   the player
   * @param progress the progress
   */
  public void updatePlayerProgress(Player player, int progress) {
    playerProgress.put(player, progress);
    player.setProgress(progress); // Update the progress in the Player object as well
  }
}