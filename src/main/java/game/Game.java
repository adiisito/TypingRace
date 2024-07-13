package game;

import java.util.List;

public interface Game {
  void startRace();

  void updatePlayerProgress(Player player, int progress);

  List<Player> getResults();

  void endRace();
}
