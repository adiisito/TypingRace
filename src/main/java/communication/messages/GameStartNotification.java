package communication.messages;

import game.TypingPlayer;
import java.util.List;

public class GameStartNotification {

  private String messageType = "GameStartNotification";
  private List<TypingPlayer> players;
  // private int indexOfCurrentPlayer;
  // private int numPlayers;
  private String text;

  public GameStartNotification(List<TypingPlayer> players, String text) {
    this.players = players;
    this.text = text;
    // this.indexOfCurrentPlayer = indexOfCurrentPlayer;
    // this.numPlayers = numPlayers;
  }

  //    public void setNumPlayers(int numPlayers) {
  //        this.numPlayers = numPlayers;
  //    }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getMessageType() {
    return messageType;
  }

  public List<TypingPlayer> getPlayers() {
    return players;
  }

  //    public int getIndexOfCurrentPlayer() {
  //        return indexOfCurrentPlayer;
  //    }

  public int getNumPlayers() {
    return players.size();
  }
}
