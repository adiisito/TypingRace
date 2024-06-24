package game;

import java.util.ArrayList;

public class GameState {
    private ArrayList<Player> players;
    private Race currentRace;
    private long startTime;

    public GameState() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public ArrayList getPlayers() {
        return players;
    }

    public void startNewRace() {
        currentRace = new Race(players);
        currentRace.startRace();
        startTime = System.currentTimeMillis();
    }

    public void endCurrentRace() {
        if (currentRace != null) {
            currentRace.endRace();
        }
    }

    public ArrayList<Player> getRaceResults() {
        if (currentRace != null) {
            return (ArrayList<Player>) currentRace.getResults();
        }
        return new ArrayList<>();
    }
    public Race getCurrentRace() {
        return currentRace;
    }

    public long getStartTime() {
        return startTime;
    }

}
