package communication.messages;

public class EndGameRequest{

    private final String messageType = "EndGameRequest";

    private String playerName;
    private long time;
    private int wpm;
    private double accuracy;


    public EndGameRequest(String playerName, long time, int wpm, double accuracy) {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }
}
