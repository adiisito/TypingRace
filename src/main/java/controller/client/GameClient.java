package controller.client;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.GameEndNotification;
import communication.messages.GameStartNotification;
import communication.messages.MessageType;
import communication.messages.PlayerJoinedNotification;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// manages network communication with server, receives and sends message
public class GameClient {

    private static final String HOSTNAME = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String DEFAULT_USERNAME = System.getProperty("username");

    private final Socket socket;
    private final PrintWriter out;
    private final ClientController clientController;

    private final Moshi moshi;
    private final JsonAdapter<MessageType> messageAdapter;

    public GameClient(ClientController clientController) throws IOException {
        this.moshi = new Moshi.Builder().build();
        this.messageAdapter = moshi.adapter(MessageType.class);

        this.clientController = clientController;

        try {
            this.socket = new Socket(HOSTNAME,SERVER_PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            startListening();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to the server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    private void startListening() {
        new Thread (() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader((socket.getInputStream())))) {
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

    public void processMessage (String message) throws IOException {

        MessageType messageObject = moshi.adapter(MessageType.class).fromJson(message);
        String messageType = messageObject.getMessageType();

        if (messageType.equals("PlayerJoinedNotification")) {
            PlayerJoinedNotification playerJoinedNotification = moshi.adapter(PlayerJoinedNotification.class).fromJson(message);
            clientController.newPlayerJoin(playerJoinedNotification);
        } else if (messageType.equals("GameStartNotification")) {
            GameStartNotification gameStartNotification = moshi.adapter(GameStartNotification.class).fromJson(message);
            clientController.handleGameStart(gameStartNotification);
        }

    }

    public void sendMessage (String message) {
        System.out.println("Sending JSON: " + message); // Log the JSON being sent
        out.println(message);
        out.flush();
    }
}
