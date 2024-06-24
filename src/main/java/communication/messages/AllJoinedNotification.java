package communication.messages;

import game.TypingPlayer;

import java.util.ArrayList;

public class AllJoinedNotification {
    String messageTye = "AllJoinedNotification";

    private ArrayList<TypingPlayer> players;

    public String getMessageType() {
        return messageTye;
    }
}
