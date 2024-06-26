package game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private ArrayList<Player> players;
    private Race currentRace;
    private long startTime;

    public GameState() {
        players = new ArrayList<>();
    }

//    public void addPlayer(Player player) {
//        players.add(player);
//    }
//
//    public void removePlayer(Player player) {
//        players.remove(player);
//    }

//    public ArrayList<Player> getPlayers() {
//        return players;
//    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<TypingPlayer> players) {
        this.players = new ArrayList<>(players);
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

    /*public ArrayList<Player> getRaceResults() {
        if (currentRace != null) {
            return (ArrayList<Player>) currentRace.getResults();
        }
        return new ArrayList<>();
    }*/

    public Race getCurrentRace() {
        return currentRace;
    }

//    public long getStartTime() {
//        return startTime;
//    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
