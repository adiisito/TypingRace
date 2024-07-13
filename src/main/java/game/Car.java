package game;

public class Car implements Player {
  private String name;
  private int wpm;
  private int progress;
  private Player player;
  private double accuracy;
  private boolean hasFinished;

  public Car(Player player) {
    this.player = player;
    this.name = player.getName();
    this.wpm = player.getWpm();
    this.progress = player.getProgress();
    this.hasFinished = false;
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
  public void setAccuracy(double accuracy) {
    this.accuracy = accuracy;
  }

  @Override
  public double getAccuracy() {
    return this.accuracy;
  }

  @Override
  public void setHasFinished(boolean hasFinished) {
    this.hasFinished = hasFinished;
  }

  @Override
  public boolean isCompleted() {
    return hasFinished;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
