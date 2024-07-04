package communication.messages;

public class GameEndNotification {
    String messageType = "GameEndNotification";
    String playerName;
    int wpm;
    int time;
    double accuracy;


    public GameEndNotification(String playerName, int wpm, double accuracy, int time) {
        this.playerName = playerName;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.time = time;

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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessageType() {
        return messageType;
    }
}
