package communication.messages;

import game.TypingPlayer;
import java.util.List;

public class RankingNotification {

  private final String messageType = "RankingNotification";

  private List<TypingPlayer> rankings;

  public RankingNotification(List<TypingPlayer> rankings) {
    this.rankings = rankings;
  }

  public List<TypingPlayer> getRankings() {
    return rankings;
  }

  public void setRankings(List<TypingPlayer> rankings) {
    this.rankings = rankings;
  }
}
