package game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Player> players;
    private Race currentRace;
    private long startTime;

    public GameState() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Race getCurrentRace() {
        return currentRace;
    }

    public void startNewRace() {
        currentRace = new Race();
    }

    public void endCurrentRace() {
        currentRace = null;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setPlayers(List<TypingPlayer> players) {
        this.players = new ArrayList<>(players);
    }
}
