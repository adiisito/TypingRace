package communication.messages;

public class AllEndedNotification {
    String messageType = "AllEndedNotification";
    int ranking;
    int time;
    int wpm;
    String playersNames;

    public AllEndedNotification() {
    }

    public AllEndedNotification(int ranking, int time, int wpm, String playersNames) {
        this.ranking = ranking;
        this.time = time;
        this.wpm = wpm;
        this.playersNames = playersNames;
    }


    public String getMessageType() {
        return messageType;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
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

    public String getPlayersNames() {
        return playersNames;
    }

    public void setPlayersNames(String playersNames) {
        this.playersNames = playersNames;
    }
}
