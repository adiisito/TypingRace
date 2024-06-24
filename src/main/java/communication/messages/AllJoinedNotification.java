package communication.messages;

import game.Player;
import game.Typer;

import java.util.List;

public class AllJoinedNotification implements Message {
    String messageTye = "AllJoinedNotification";

    private List<Typer> players;


    @Override
    public String getMessageType() {
        return messageTye;
    }
}
