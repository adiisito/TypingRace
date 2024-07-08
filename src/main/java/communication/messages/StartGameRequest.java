package communication.messages;

public class StartGameRequest {

    private final String messageType = "StartGameRequest";
    private String hostPlayerName;

    public StartGameRequest(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
    }

    public String getHostPlayerName() {
        return hostPlayerName;
    }

    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
    }
}
