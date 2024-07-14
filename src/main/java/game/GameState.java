package game;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Game state.
 */
public class GameState {
    private List<TypingPlayer> players;
    private Race currentRace;
    private long startTime;

    /**
     * Instantiates a new Game state.
     */
    public GameState() {
        players = new ArrayList<>();
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(TypingPlayer player) {
        players.add(player);
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<TypingPlayer> getPlayers() {
        return players;
    }

    /**
     * Gets current race.
     *
     * @return the current race
     */
    public Race getCurrentRace() {
        return currentRace;
    }

    /**
     * Start new race.
     */
    public void startNewRace() {
        currentRace = new Race();
    }

    /**
     * End current race.
     */
    public void endCurrentRace() {
        currentRace = null;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets players.
     *
     * @param players the players
     */
    public void setPlayers(List<TypingPlayer> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * Gets completed players.
     *
     * @return the completed players
     */
    public List<TypingPlayer> getCompletedPlayers() {
        List<TypingPlayer> completedPlayers = new ArrayList<>();
        for (TypingPlayer player : players) {
            if (player.isCompleted()) {
                completedPlayers.add(player);
            }
        }
        return completedPlayers;
    }
}
