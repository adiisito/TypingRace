
package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;
import communication.messages.MessageType;
import communication.messages.PlayerJoinedNotification;
import communication.messages.PlayerLeftNotification;

public class ConnectionManager extends Thread {

    private final Socket clientSocket;
    private final GameServer server;
    private BufferedReader in;
    private PrintWriter out;
    private final JsonAdapter<MessageType> messageAdapter;
    private final Moshi moshi = new Moshi.Builder().build();
    private String playerName;

    public ConnectionManager(Socket socket, GameServer server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.messageAdapter = moshi.adapter(MessageType.class);
    }

    @Override
    public void run() {
        try {
            String messageLine;
            while ((messageLine = in.readLine()) != null) {
                System.out.println("Received from client: " + messageLine);
                processMessage(messageLine);
            }
        } catch (IOException exception) {
            System.out.println("Error reading client, player disconnected");
            server.removePlayer(playerName);
            exception.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message) throws IOException {
        MessageType messageObject = moshi.adapter(MessageType.class).fromJson(message);
        String messageType = messageObject.getMessageType();

        if (messageType.equals("JoinGameRequest")) {
            System.out.println("Received JoinGameRequest");
            JoinGameRequest joinGameRequest = moshi.adapter(JoinGameRequest.class).fromJson(message);
            handleJoinGameRequest(joinGameRequest);
        } else if (messageType.equals("StartGameRequest")) {
            System.out.println("Received StartGameRequest");
            server.startGame();
        } else if (messageType.equals("PlayerLeftNotification")) {
            PlayerLeftNotification leftNotification = moshi.adapter(PlayerLeftNotification.class).fromJson(message);
            server.removePlayer(leftNotification.getPlayerName());
        }
        // @yili and @yuanyuan, please add other notifs according to the need!
    }

    private void handleJoinGameRequest(JoinGameRequest request) {
        this.playerName = request.getPlayerName();
        System.out.println("Handle join game request for " + playerName);

        PlayerJoinedNotification notification = new PlayerJoinedNotification(playerName, server.getClients().size());
        String json = moshi.adapter(PlayerJoinedNotification.class).toJson(notification);
        server.broadcastMessage(json);
        server.addPlayer(playerName);
    }

    public void sendMessage(String message) {
        System.out.println("Sending JSON: " + message);
        out.println(message);
        out.flush();
    }

    public String getPlayerName() {
        return playerName;
    }
}
