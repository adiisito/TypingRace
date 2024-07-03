package game;

public class Car implements Player {
    private String id;
    private String name;
    private int wpm;
    private int progress;
    private Player player;
    private double accuracy;


    public Car(Player player) {
        this.player = player;
        this.id = player.getId(); // Assuming Player interface has getId()
        this.name = player.getName();
        this.wpm = player.getWpm();
        this.progress = player.getProgress();
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
        player.setProgress(progress);
    }

    @Override
    public void receiveProgressUpdate(String playerName, int progress, int wpm) {
        System.out.println(playerName + " progress: " + progress + ", WPM: " + wpm);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public double getAccuracy() {
        return this.accuracy;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}