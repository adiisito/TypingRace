package communication.messages;

public class GameStateNotification {
    String messageType = "GameStateNotification";
    String playerName;
    int wpm;
    int time;
    int progress;
    double accuracy;


    public GameStateNotification(String playerName, int wpm, int time, int progress, double accuracy) {
        this.playerName = playerName;
        this.wpm = wpm;
        this.time = time;
        this.progress = progress;
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
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
