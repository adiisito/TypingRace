
package controller.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.GameStartNotification;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.PlayerLeftNotification;
import communication.messages.LobbyFullNotification;
import game.TypingPlayer;

public class GameServer {

    private static final int SERVER_PORT = 8080;
    private final ServerSocket serverSocket;
    private final List<ConnectionManager> clients;
    private final List<String> playerNames;
    private final Moshi moshi;
    private final JsonAdapter<GameStartNotification> gameStartNotificationAdapter;
    private final JsonAdapter<PlayerListUpdateNotification> playerListUpdateNotificationAdapter;
    private final JsonAdapter<PlayerLeftNotification> playerLeftNotificationAdapter;
    private final JsonAdapter<LobbyFullNotification> lobbyFullNotificationAdapter;

    public GameServer() throws IOException {
        this.serverSocket = new ServerSocket(SERVER_PORT);
        this.clients = new ArrayList<>();
        this.playerNames = new ArrayList<>();
        this.moshi = new Moshi.Builder().build();
        this.gameStartNotificationAdapter = moshi.adapter(GameStartNotification.class);
        this.playerListUpdateNotificationAdapter = moshi.adapter(PlayerListUpdateNotification.class);
        this.playerLeftNotificationAdapter = moshi.adapter(PlayerLeftNotification.class);
        this.lobbyFullNotificationAdapter = moshi.adapter(LobbyFullNotification.class);
        System.out.println("Server started, listening...");
    }

    public void start() {
        System.out.println("Waiting for clients...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected...");
                ConnectionManager connectionManager = new ConnectionManager(clientSocket, this);
                clients.add(connectionManager);
                connectionManager.start();
            } catch (IOException e) {
                System.out.println("Error connecting to client!");
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message) {
        for (ConnectionManager client : clients) {
            client.sendMessage(message);
        }
    }

    public void broadcastPlayerListUpdate() {
        PlayerListUpdateNotification updateNotification = new PlayerListUpdateNotification(playerNames);
        String json = playerListUpdateNotificationAdapter.toJson(updateNotification);
        broadcastMessage(json);
    }

    public void addPlayer(String playerName) {
        playerNames.add(playerName);
        broadcastPlayerListUpdate();
        if (playerNames.size() == 6) {
            broadcastLobbyFull();
        }
    }

    public void removePlayer(String playerName) {
        playerNames.remove(playerName);
        PlayerLeftNotification leftNotification = new PlayerLeftNotification(playerName);
        String json = playerLeftNotificationAdapter.toJson(leftNotification);
        broadcastMessage(json);
        broadcastPlayerListUpdate();
    }

    private void broadcastLobbyFull() {
        LobbyFullNotification lobbyFullNotification = new LobbyFullNotification();
        String json = lobbyFullNotificationAdapter.toJson(lobbyFullNotification);
        broadcastMessage(json);
    }

    public void startGame() {
        System.out.println("Starting the game...");
        List<TypingPlayer> players = new ArrayList<>();
        for (String playerName : playerNames) {
            players.add(new TypingPlayer(playerName));
        }
        GameStartNotification gameStartNotification = new GameStartNotification(players, 0); // Assuming the first player is the current player
        String json = gameStartNotificationAdapter.toJson(gameStartNotification);
        broadcastMessage(json);
    }

    public List<ConnectionManager> getClients() {
        return clients;
    }

    public static void main(String[] args) {
        try {
            GameServer server = new GameServer();
            server.start();
        } catch (IOException e) {
            System.out.println("Failed to start the server");
            e.printStackTrace();
        }
    }
}
