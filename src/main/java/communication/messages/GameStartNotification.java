package communication.messages;

import game.Player;

import java.util.ArrayList;

public class GameStartNotification {
    String messageType = "GameStartNotification";
    String text;
    int numPlayers;
    ArrayList<Player> players;
    int indexOfCurrentPlayer;

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

    public int getIndexOfCurrentPlayer() {
        return indexOfCurrentPlayer;
    }

    public void setIndexOfCurrentPlayer(int indexOfCurrentPlayer) {
        this.indexOfCurrentPlayer = indexOfCurrentPlayer;
    }
}
