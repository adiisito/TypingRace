package communication.messages;

public class PlayerLeftRequest {

    private final String message = "PlayerLeftRequest";

    private String playerName;

    public PlayerLeftRequest(String playerName) {
        this.playerName = playerName;
    }

    public String getMessage() {
        return message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
