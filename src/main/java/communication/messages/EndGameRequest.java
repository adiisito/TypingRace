package communication.messages;

public class EndGameRequest{

    private final String messageType = "EndGameRequest";

    private String playerName;
    private int time;
    private int wpm;
    private double accuracy;


    public EndGameRequest(String playerName, int time, int wpm, double accuracy) {
        this.playerName = playerName;
        this.time = time;
        this.wpm = wpm;
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }
}
