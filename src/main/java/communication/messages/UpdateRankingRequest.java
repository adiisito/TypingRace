package communication.messages;

import game.TypingPlayer;
import java.util.List;

public class UpdateRankingRequest {

  private final String messageType = "UpdateRankingRequest";

  private List<TypingPlayer> players;

  public UpdateRankingRequest(List<TypingPlayer> players) {
    this.players = players;
  }

  public List<TypingPlayer> getPlayers() {
    return players;
  }

  public void setPlayers(List<TypingPlayer> players) {
    this.players = players;
  }
}
