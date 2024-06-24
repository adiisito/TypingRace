package communication.messages;

public class UpdateProgressRequest {

    private final String messageType = "UpdateProgressRequest";

    private String playerName;
    private int wpm;
    private int time;
    private int progress;

    public UpdateProgressRequest(String playerName, int wpm, int time, int progress) {
        this.playerName = playerName;
        this.wpm = wpm;
        this.time = time;
        this.progress = progress;
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

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
