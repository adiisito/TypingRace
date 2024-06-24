package communication.messages;

public class GameStateNotification extends Message {
    String messageType = "GameStateNotification";
    String playerName;
    int wpm;
    int time;
    int progress;



    public GameStateNotification(String playerName, int wpm, int time, int progress) {
        this.playerName = playerName;
        this.wpm = wpm;
        this.time = time;
        this.progress = progress;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWpm() {
        return wpm;
    }

    public int getTime() {
        return time;
    }

    public int getProgress() {
        return progress;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMessageType() {
        return messageType;
    }
}
