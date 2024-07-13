package game;

import java.util.HashMap;
import java.util.Map;

public class Race {
  private Map<Player, Integer> playerProgress;

  public Race() {
    playerProgress = new HashMap<>();
  }

  public void updatePlayerProgress(Player player, int progress) {
    playerProgress.put(player, progress);
    player.setProgress(progress); // Update the progress in the Player object as well
  }

  public int getPlayerProgress(Player player) {
    return playerProgress.getOrDefault(player, 0);
  }
}
