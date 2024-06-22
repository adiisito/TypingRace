package game;

public interface Player {
    String getName();
    int getWpm();
    void setWpm(int wpm);
    int getProgress();
    void setProgress(int progress);
}
