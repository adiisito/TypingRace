package game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
  private List<TypingPlayer> players;
  private Race currentRace;
  private long startTime;

  public GameState() {
    players = new ArrayList<>();
  }

  public void addPlayer(TypingPlayer player) {
    players.add(player);
  }

  public List<TypingPlayer> getPlayers() {
    return players;
  }

  public Race getCurrentRace() {
    return currentRace;
  }

  public void startNewRace() {
    currentRace = new Race();
  }

  public void endCurrentRace() {
    currentRace = null;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setPlayers(List<TypingPlayer> players) {
    this.players = new ArrayList<>(players);
  }

  public List<TypingPlayer> getCompletedPlayers() {
    List<TypingPlayer> completedPlayers = new ArrayList<>();
    for (TypingPlayer player : players) {
      if (player.isCompleted()) {
        completedPlayers.add(player);
      }
    }
    return completedPlayers;
  }
}
