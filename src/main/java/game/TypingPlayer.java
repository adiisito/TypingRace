package game;

public class TypingPlayer implements Player {
    private String name;
    private int wpm;
    private int progress;

    public TypingPlayer(String name) {
        this.name = name;
        this.wpm = 0;
        this.progress = 0;
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
