
package controller.client;

import com.squareup.moshi.Moshi;
import communication.messages.*;
import game.GameState;
import game.Player;
import game.TypingPlayer;
import view.ClientWindow;
import view.GUI;
import view.GameScreen;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * The type Client controller.
 */
public class ClientController {

    public GameClient clientModel;
    private GameState gameState;
    private List<TypingPlayer> players;
    private GameScreen view;
    private int numPlayers;
    private final Moshi moshi = new Moshi.Builder().build();
    private ClientWindow clientWindow;
    private GUI mainGui;
    private TypingPlayer currentPlayer;

    public ClientController(ClientWindow clientWindow, GUI mainGui) {
        this.clientWindow = clientWindow;
        this.mainGui = mainGui;
    }

    /**
     * Attempts to join a game with the specified player name.
     *
     * @param playerName the name of player
     * @throws IOException if there is an issue sending the join game request over the network
     */
    public void joinGame(String playerName) throws IOException {
        this.clientModel = new GameClient(this, playerName);
        JoinGameRequest joinRequest = new JoinGameRequest(playerName);
        String json = moshi.adapter(JoinGameRequest.class).toJson(joinRequest);
        clientModel.sendMessage(json);
        System.out.println("Welcome " + playerName + ". You joined the game");

        this.currentPlayer = new TypingPlayer(playerName);
    }

    /**
     * Handles updates to the list of players in the game, updating the client window and GUI accordingly.
     *
     * @param updateNotification the notification containing the update list of players
     */
    public void handlePlayerListUpdate(PlayerListUpdateNotification updateNotification) {
        List<String> playerNames = updateNotification.getPlayerNames();
        clientWindow.updatePlayerList(playerNames);
        mainGui.updateAllClientWindows(playerNames);
        if (playerNames.size() == 6) {
            clientWindow.showLobbyFullButton();
        }
    }

    /**
     * Handle game start notification, initializing game state and updating the UI to reflect the game start.
     *
     * @param gameStartNotification the game start notification
     */
    public synchronized void handleGameStart(GameStartNotification gameStartNotification) {
        this.players = gameStartNotification.getPlayers();
        this.gameState = new GameState();
        this.numPlayers = gameStartNotification.getNumPlayers();

        // this.currentPlayer = players.get(gameStartNotification.getIndexOfCurrentPlayer());

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(this.clientModel.getPlayerName())) {
                this.currentPlayer = players.get(i);
                break;
            }
        }


        this.gameState.setPlayers(players);
        this.view = new GameScreen(this.gameState, currentPlayer, this);
        this.gameState.startNewRace();
        this.view.addCars();
        SwingUtilities.invokeLater(() -> {
            clientWindow.setContentPane(view);
            clientWindow.revalidate();
            clientWindow.repaint();
        });
        System.out.println("Game started with " + players.size() + " players.");
    }

    /**
     * Sends a player left request for the specified player.
     *
     * @param playerName the player name
     */
    public void playerLeft(String playerName){
        PlayerLeftRequest request = new PlayerLeftRequest(playerName);
        String json = moshi.adapter(PlayerLeftRequest.class).toJson(request);

        clientModel.sendMessage(json);
    }

    /**
     * Handle player left notification, updating the UI to reflect this change.
     *
     * @param leftNotification the left notification
     */
    public void handlePlayerLeft(PlayerLeftNotification leftNotification) {
        SwingUtilities.invokeLater(() -> {
            String playerName = leftNotification.getPlayerName();
            clientWindow.showPlayerLeftMessage(playerName);
            if (mainGui != null) {
                mainGui.updateAllClientWindowsWithPlayerLeft(playerName);
            }
        });
    }

    /**
     * Start game.
     */
    public void startGame() {

        StartGameRequest request = new StartGameRequest();
        String json = moshi.adapter(StartGameRequest.class).toJson(request);

        clientModel.sendMessage(json);
        System.out.println("Game started");
    }


    /**
     * Sends an update of the player's progress to the server.
     *
     * @param playerName the player name
     * @param wpm word per minute rate
     * @param progress current progress
     * @param accuracy current accuracy
     * @param time current time elapsed in the game
     */
    public void updateProgress (String playerName, int wpm, int progress, double accuracy, int time){

        UpdateProgressRequest progressRequest = new UpdateProgressRequest(currentPlayer.getName(), wpm, progress, accuracy,time);
        String json = moshi.adapter(UpdateProgressRequest.class).toJson(progressRequest);
        clientModel.sendMessage(json);
    }

    /**
     * Handles updates to the game state received from server.
     *
     * @param notification game state notification
     */
    public void handleProgress (GameStateNotification notification) {

        view.updateCarPositions(notification.getPlayerName(), notification.getProgress());
        SwingUtilities.invokeLater(() -> {

            if (view != null) {

                Player player = findPlayerByName(notification.getPlayerName());

                if (player != null) {
                    // update the game screen with the latest progress, wpm, and accuracy
                    currentPlayer.setProgress(notification.getProgress());
                    currentPlayer.setWpm(notification.getWpm());
                    currentPlayer.setAccuracy(notification.getAccuracy());

                    if (notification.getPlayerName().equals(currentPlayer.getName())) {
                        view.updateProgressDisplay(notification.getWpm(), notification.getAccuracy());
                    }


                }

            }
        });
    }

    public Player findPlayerByName (String playerName) {
        for (Player player: gameState.getPlayers()) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Sends a request to end the game for the player with the end status.
     *
     * @param playerName player name
     * @param time the total time to finish the game
     * @param wpm words per minute rate
     * @param accuracy the end accuracy
     */
    public void endGame(String playerName, int time, int wpm, double accuracy) {

        EndGameRequest request = new EndGameRequest(playerName, time, wpm, accuracy);
        String json = moshi.adapter(EndGameRequest.class).toJson(request);
        clientModel.sendMessage(json);

    }

    /**
     * Handles the notification of the end of the game.
     *
     * @param notification game end notification
     */
    public void handleGameEnd (GameEndNotification notification) {

        System.out.println("Game ended");


    }

    /**
     * Displays the lobby full button on the client window.
     */
    void handleLobbyFull() {
        SwingUtilities.invokeLater(() -> clientWindow.showLobbyFullButton());
    }

    public String getCurrentPlayerName() {
        return currentPlayer.getName();
    }
}
