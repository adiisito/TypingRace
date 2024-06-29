package game;

import java.util.UUID;

public class TypingPlayer implements Player {
    private String name;
    private int wpm;
    private int progress;
    private double accuracy;

    private String id;

    public TypingPlayer(String name) {
        this.name = name;
        this.wpm = 0;
        this.progress = 0;
        this.accuracy = 1;
        this.id = UUID.randomUUID().toString();
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWpm() {
        return wpm;
    }

    @Override
    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void receiveProgressUpdate(String playerName, int progress, int wpm) {
        System.out.println(playerName + " progress: " + progress + ", WPM: " + wpm);
    }
}
