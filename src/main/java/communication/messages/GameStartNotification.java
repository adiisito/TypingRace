
package communication.messages;

import java.util.List;
import game.TypingPlayer;

public class GameStartNotification {

    private String messageType = "GameStartNotification";
    private List<TypingPlayer> players;
    private String text;


    public GameStartNotification(List<TypingPlayer> players, String text) {
        this.players = players;
        this.text = text;
    }

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

    public int getNumPlayers() {
        return players.size();
    }
}
