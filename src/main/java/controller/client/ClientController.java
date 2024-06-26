
package controller.client;

import com.squareup.moshi.Moshi;
import communication.messages.GameStartNotification;
import communication.messages.JoinGameRequest;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.PlayerLeftNotification;
import communication.messages.MessageType;
import game.GameState;
import game.TypingPlayer;
import view.ClientWindow;
import view.GUI;
import view.GameScreen;

import javax.swing.*;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.List;

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

    public void handlePlayerLeft(PlayerLeftNotification leftNotification) {
        SwingUtilities.invokeLater(() -> {
            String playerName = leftNotification.getPlayerName();
            clientWindow.showPlayerLeftMessage(playerName);
            if (mainGui != null) {
                mainGui.updateAllClientWindowsWithPlayerLeft(playerName);
            }
        });
    }

    public void startGame() {
        clientModel.sendMessage("{\"messageType\":\"StartGameRequest\"}");
    }

//    public void processMessage(String message) throws IOException {
//        MessageType messageObject = moshi.adapter(MessageType.class).fromJson(message);
//        String messageType = messageObject.getMessageType();
//
//        if (messageType.equals("PlayerListUpdateNotification")) {
//            PlayerListUpdateNotification updateNotification = moshi.adapter(PlayerListUpdateNotification.class).fromJson(message);
//            handlePlayerListUpdate(updateNotification);
//        } else if (messageType.equals("GameStartNotification")) {
//            GameStartNotification gameStartNotification = moshi.adapter(GameStartNotification.class).fromJson(message);
//            handleGameStart(gameStartNotification);
//        } else if (messageType.equals("LobbyFullNotification")) {
//            handleLobbyFull();
//        } else if (messageType.equals("PlayerLeftNotification")) {
//            PlayerLeftNotification leftNotification = moshi.adapter(PlayerLeftNotification.class).fromJson(message);
//            handlePlayerLeft(leftNotification);
//        }
//    }

    void handleLobbyFull() {
        SwingUtilities.invokeLater(() -> clientWindow.showLobbyFullButton());
    }
}
