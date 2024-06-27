
package communication.messages;

import java.util.List;
import game.TypingPlayer;

public class GameStartNotification {

    private String messageType = "GameStartNotification";
    private List<TypingPlayer> players;
    private int indexOfCurrentPlayer;
    private int numPlayers;

    public GameStartNotification(List<TypingPlayer> players, int indexOfCurrentPlayer, int numPlayers) {
        this.players = players;
        this.indexOfCurrentPlayer = indexOfCurrentPlayer;
        this.numPlayers = numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public String getMessageType() {
        return messageType;
    }

    public List<TypingPlayer> getPlayers() {
        return players;
    }

    public int getIndexOfCurrentPlayer() {
        return indexOfCurrentPlayer;
    }

    public int getNumPlayers() {
        return players.size();
    }
}
