
package communication.messages;

import java.util.List;
import game.TypingPlayer;

/**
 * The type Game start notification.
 */
public class GameStartNotification {

    private String messageType = "GameStartNotification";
    private List<TypingPlayer> players;
    private String text;


    /**
     * Instantiates a new Game start notification.
     *
     * @param players the players
     * @param text    the text
     */
    public GameStartNotification(List<TypingPlayer> players, String text) {
        this.players = players;
        this.text = text;
    }


    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets message type.
     *
     * @return the message type
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<TypingPlayer> getPlayers() {
        return players;
    }


    /**
     * Gets num players.
     *
     * @return the num players
     */
    public int getNumPlayers() {
        return players.size();
    }
}
