package communication.messages;

import game.Player;
import game.TypingPlayer;

import java.util.ArrayList;
import java.util.List;

public class AllJoinedNotification extends Message {
    String messageTye = "AllJoinedNotification";

    private ArrayList<TypingPlayer> players;

    public String getMessageType() {
        return messageTye;
    }
}
