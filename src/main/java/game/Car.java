package game;

public class Car implements Player {
    private String name;
    private int wpm;
    private int progress;


    public Car(Player player) {
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