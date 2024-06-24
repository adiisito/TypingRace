package communication.messages;

public class JoinGameRequest extends Message {

    private final String messageType = "JoinGameRequest";

    private String playerName;

    public JoinGameRequest(String playerName) {
        this.playerName = playerName;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
