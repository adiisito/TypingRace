
package controller.client;

import com.squareup.moshi.Moshi;
import communication.messages.GameStartNotification;
import communication.messages.JoinGameRequest;
import communication.messages.PlayerLeftRequest;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.PlayerLeftNotification;
import communication.messages.MessageType;
import communication.messages.StartGameRequest;
import communication.messages.UpdateProgressRequest;
import game.GameState;
import game.TypingPlayer;
import view.ClientWindow;
import view.GUI;
import view.GameScreen;

import javax.swing.*;
import java.io.IOException;
//import java.util.ArrayList;
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

    public ClientController(ClientWindow clientWindow, GUI mainGui) {
        this.clientWindow = clientWindow;
        this.mainGui = mainGui;
    }

    public void joinGame(String playerName) throws IOException {
        this.clientModel = new GameClient(this);
        JoinGameRequest joinRequest = new JoinGameRequest(playerName);
        String json = moshi.adapter(JoinGameRequest.class).toJson(joinRequest);
        clientModel.sendMessage(json);
        System.out.println("Welcome " + playerName + ". You joined the game");
    }

    public void handlePlayerListUpdate(PlayerListUpdateNotification updateNotification) {
        List<String> playerNames = updateNotification.getPlayerNames();
        clientWindow.updatePlayerList(playerNames);
        mainGui.updateAllClientWindows(playerNames);
        if (playerNames.size() == 6) {
            clientWindow.showLobbyFullButton();
        }
    }

    /**
     * Handle game start notification.
     *
     * @param gameStartNotification the game start notification
     */
    public void handleGameStart(GameStartNotification gameStartNotification) {
        this.players = gameStartNotification.getPlayers();
        this.gameState = new GameState();
        this.numPlayers = gameStartNotification.getNumPlayers();

        this.gameState.setPlayers(players);
        this.view = new GameScreen(this.gameState, players.get(gameStartNotification.getIndexOfCurrentPlayer()));
        SwingUtilities.invokeLater(() -> {
            clientWindow.setContentPane(view);
            clientWindow.revalidate();
            clientWindow.repaint();
        });
        System.out.println("Game started");
    }

    /**
     * Player left.
     *
     * @param playerName the player name
     */
    public void playerLeft(String playerName){
        PlayerLeftRequest request = new PlayerLeftRequest(playerName);
        String json = moshi.adapter(PlayerLeftRequest.class).toJson(request);

        clientModel.sendMessage(json);
    }

    /**
     * Handle player left notification.
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


    public void updateProgress (String playerName, int wpm, int progress, double accuracy){

        UpdateProgressRequest progressRequest = new UpdateProgressRequest(playerName, wpm, progress, accuracy);
        String json = moshi.adapter(UpdateProgressRequest.class).toJson(progressRequest);
        clientModel.sendMessage(json);
    }


    void handleLobbyFull() {
        SwingUtilities.invokeLater(() -> clientWindow.showLobbyFullButton());
    }
}
