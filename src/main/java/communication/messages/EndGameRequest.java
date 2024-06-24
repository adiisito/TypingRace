package communication.messages;

public class EndGameRequest{

    private final String messageType = "EndGameRequest";

    private String playerName;

    public EndGameRequest(String playerName) {
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
