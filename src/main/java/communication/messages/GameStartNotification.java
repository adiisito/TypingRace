
package communication.messages;

import java.util.List;
import game.TypingPlayer;

public class GameStartNotification {

    private String messageType = "GameStartNotification";
    private List<TypingPlayer> players;
    private int indexOfCurrentPlayer;

    public GameStartNotification(List<TypingPlayer> players, int indexOfCurrentPlayer) {
        this.players = players;
        this.indexOfCurrentPlayer = indexOfCurrentPlayer;
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
