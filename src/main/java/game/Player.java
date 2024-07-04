package game;

public interface Player {
    String getName();
    int getWpm();
    void setWpm(int wpm);
    int getProgress();
    void setProgress(int progress);
    void receiveProgressUpdate(String playerName, int progress, int wpm);
    String getId();
    void setAccuracy(double accuracy);
    double getAccuracy();
}

