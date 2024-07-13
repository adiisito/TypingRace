package communication.messages;

public class StartGameRequest {

    private final String messageType = "StartGameRequest";
    private String hostPlayerName;
    private String providedText;

    public StartGameRequest(String hostPlayerName, String providedText) {
        this.hostPlayerName = hostPlayerName;
        this.providedText = providedText;
    }

    public String getHostPlayerName() {
        return hostPlayerName;
    }

    public void setHostPlayerName(String hostPlayerName) {
        this.hostPlayerName = hostPlayerName;
    }

    public String getProvidedText() {
        return providedText;
    }

    public void setProvidedText(String providedText) {
        this.providedText = providedText;
    }
}
