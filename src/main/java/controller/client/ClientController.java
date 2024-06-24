package controller.client;

import communication.messages.GameStartNotification;
import communication.messages.JoinGameRequest;
import communication.messages.PlayerJoinedNotification;
import game.GameState;
import game.Player;
import view.GUI;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

// initialises the view and model, handles messages received from the server and updates the view
public class ClientController {

    private GameClient clientModel;
    private GameState gameState;
    private ArrayList<Player> players;
    private GUI view;
    private int numPlayers;

    public ClientController() {
    }

    public void joinGameRequest(String playerName) throws IOException {
        this.clientModel = new GameClient(this);

        JoinGameRequest joinRequest = new JoinGameRequest(playerName);
        clientModel.sendMessage(joinRequest);
    }

    public void newPlayerJoin (PlayerJoinedNotification playerJoinedNotification) {
        SwingUtilities.invokeLater(() -> {
            //waitingView updates Text (new player: playerName)
        });
    }

    public void handleGameStart(GameStartNotification gameStartNotification) {

        //init game model
        this.players = gameStartNotification.getPlayers();
        this.gameState = new GameState();
        this.numPlayers = gameStartNotification.getNumPlayers();

        this.gameState.setPlayers(players);
        this.view = new GUI(this.gameState);
        view.setVisible(true);

    }
}
