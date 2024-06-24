package communication.messages;

import game.Player;
import game.TypingPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameStartNotification extends Message {
    String messageType = "GameStartNotification";
    String text;
    int numPlayers;
    ArrayList<Player> players;

    public GameStartNotification(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getMessageType() {
        return messageType;
    }
}
