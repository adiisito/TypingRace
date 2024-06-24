package communication.messages;

public class GameEndNotification implements Message {
    String messageType =" GameEndNotification";
    String playerName;
    int ranking;
    int wpm;
    int time;
    int progress;

    public GameEndNotification(String playerName, int ranking, int wpm, int time, int progress) {
        this.playerName = playerName;
        this.ranking = ranking;
        this.wpm = wpm;
        this.time = time;
        this.progress = progress;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getRanking() {
        return ranking;
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

    public void setRanking(int ranking) {
        this.ranking = ranking;
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
