package game;

/** The type Typing player. */
public class TypingPlayer implements Player {
  private final String name;
  private int wpm;
  private int progress;
  private double accuracy;
  private boolean hasFinished = false;

  // private String id;

  /**
   * Instantiates a new Typing player.
   *
   * @param name the name
   */
  public TypingPlayer(String name) {
    this.name = name;
    this.wpm = 0;
    this.progress = 0;
    this.accuracy = 1;
    // this.id = UUID.randomUUID().toString();
  }

  public double getAccuracy() {
    return accuracy;
  }

  @Override
  public void setAccuracy(double accuracy) {
    this.accuracy = accuracy;
  }

  @Override
  public void setHasFinished(boolean hasFinished) {
    this.hasFinished = hasFinished;
  }

  @Override
  public boolean isCompleted() {
    return hasFinished;
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
