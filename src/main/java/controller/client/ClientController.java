package controller.client;

import com.squareup.moshi.Moshi;
import communication.messages.GameStartNotification;
import communication.messages.JoinGameRequest;
import communication.messages.PlayerJoinedNotification;
import game.GameState;
import game.Player;
import view.GUI;
import view.GameScreen;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

// initialises the view and model, handles messages received from the server and updates the view
public class ClientController {

    private GameClient clientModel;
    private GameState gameState;
    private ArrayList<Player> players;
    private GameScreen view;
    private int numPlayers;
    private final Moshi moshi = new Moshi.Builder().build();

    public ClientController() {
    }

    // When a player presses a button to start a game
    // initiate communication with the server, send JoinGameRequest to server
    public void joinGame(String playerName) throws IOException {
        this.clientModel = new GameClient(this);

        JoinGameRequest joinRequest = new JoinGameRequest(playerName);
        String json = moshi.adapter(JoinGameRequest.class).toJson(joinRequest);
        clientModel.sendMessage(json);

        System.out.println("Welcome " + playerName + ". you joined the game");
    }

    // handle playerjoinednoti -> update waitingView
    public void newPlayerJoin (PlayerJoinedNotification playerJoinedNotification) {
        SwingUtilities.invokeLater(() -> {
            //waitingView updates Text (new player: playerName)
        });
    }

    // handle gamestartnoti -> initialize and set the game state and GameScreen at the start of the game
    public void handleGameStart(GameStartNotification gameStartNotification) {

        //init game model
        this.players = gameStartNotification.getPlayers();
        this.gameState = new GameState();
        this.numPlayers = gameStartNotification.getNumPlayers();

        //init game view
        this.gameState.setPlayers(players);
        this.view = new GameScreen(this.gameState, players.get(gameStartNotification.getIndexOfCurrentPlayer()));
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });

        System.out.println("game started");
    }
}
