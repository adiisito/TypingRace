
package controller.client;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import communication.messages.GameEndNotification;
import communication.messages.GameStartNotification;
import communication.messages.GameStateNotification;
import communication.messages.MessageType;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.PlayerLeftNotification;
import communication.messages.LobbyFullNotification;
import communication.messages.RankingNotification;
import game.Player;
import game.TypingPlayer;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {

    private static final String HOSTNAME = "127.0.0.100";
    private static final int SERVER_PORT = 8080;

    private final Socket socket;
    private final PrintWriter out;
    private final ClientController clientController;

    private final Moshi moshi;

    private String playerName;

    public GameClient(ClientController clientController, String playerName) throws IOException {
        this.moshi = new Moshi.Builder().build();

        this.clientController = clientController;

        this.playerName = playerName;

        try {
            this.socket = new Socket(HOSTNAME, SERVER_PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            startListening();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    private void startListening() {
        new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.isEmpty()) {
                        processMessage(message);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while listening to server messages: " + e.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error closing socket: " + e.getMessage(), "Socket Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    public void processMessage(String message) throws IOException {
        MessageType messageObject = moshi.adapter(MessageType.class).fromJson(message);
        String messageType = messageObject.getMessageType();

        System.out.println("received messageType: " + messageType);

        if (messageType.equals("PlayerListUpdateNotification")) {
            PlayerListUpdateNotification updateNotification = moshi.adapter(PlayerListUpdateNotification.class).fromJson(message);
            clientController.handlePlayerListUpdate(updateNotification);
        } else if (messageType.equals("GameStartNotification")) {
            GameStartNotification gameStartNotification = moshi.adapter(GameStartNotification.class).fromJson(message);
            clientController.handleGameStart(gameStartNotification);
        } else if (messageType.equals("LobbyFullNotification")) {
            clientController.handleLobbyFull();
        } else if (messageType.equals("PlayerLeftNotification")) {
            PlayerLeftNotification leftNotification = moshi.adapter(PlayerLeftNotification.class).fromJson(message);
            clientController.handlePlayerLeft(leftNotification);
        } else if (messageType.equals("GameStateNotification")) {
            GameStateNotification stateNotification = moshi.adapter(GameStateNotification.class).fromJson(message);
            clientController.handleProgress(stateNotification);
        } else if (messageType.equals("GameEndNotification")) {
            GameEndNotification gameEndNotification = moshi.adapter(GameEndNotification.class).fromJson(message);
            clientController.handleGameEnd(gameEndNotification);
        } else if (messageType.equals("RankingNotification")) {
            RankingNotification rankingNotification = moshi.adapter(RankingNotification.class).fromJson(message);
            clientController.handleRankingNotification(rankingNotification);
        }

    }

    public void sendMessage(String message) {
        System.out.println("Sending JSON: " + message);
        out.println(message);
        out.flush();
    }

    public String getPlayerName() {

        return clientController.getCurrentPlayerName();
    }
}
