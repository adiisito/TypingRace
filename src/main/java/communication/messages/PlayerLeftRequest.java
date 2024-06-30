package communication.messages;

public class PlayerLeftRequest {

    private final String messageType = "PlayerLeftRequest";

    private String playerName;

    public PlayerLeftRequest(String playerName) {
        this.playerName = playerName;
    }

    public String getMessage() {
        return messageType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
