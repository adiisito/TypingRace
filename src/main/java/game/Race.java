package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Race implements Game {
    private List<Player> players;
    private String text;
    private ExecutorService executorService;
    private volatile boolean isRaceActive;
    private long startTime;

    public Race(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.text = Text.getRandomText();
        this.executorService = Executors.newFixedThreadPool(players.size());
        this.isRaceActive = false;
    }

    @Override
    public void startRace() {
        isRaceActive = true;
        startTime = System.currentTimeMillis();
        for (Player player : players) {
            executorService.submit(() -> runRaceForPlayer(player));
        }
    }

    private void runRaceForPlayer(Player player) {
        while (isRaceActive && player.getProgress() < text.length()) {
            // Simulate typing progress
            int newProgress = player.getProgress() + 1;
            updatePlayerProgress(player, newProgress);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void updatePlayerProgress(Player player, int progress) {
        player.setProgress(progress);
        player.setWpm(calculateWpm(player));
        broadcastProgress(player);
    }

    private int calculateWpm(Player player) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        double elapsedTimeInMinutes = elapsedTime / 60000.0;
        int wordsTyped = player.getProgress() / 5;
        return (int) (wordsTyped / elapsedTimeInMinutes);
    }

    private void broadcastProgress(Player player) {
        for (Player p : players) {
            p.receiveProgressUpdate(player.getName(), player.getProgress(), player.getWpm());
        }
    }

    @Override
    public List<Player> getResults() {
        players.sort((p1, p2) -> Integer.compare(p2.getWpm(), p1.getWpm()));
        return players;
    }

    @Override
    public void endRace() {
        isRaceActive = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public long getStartTime() {
        return startTime;
    }
}
