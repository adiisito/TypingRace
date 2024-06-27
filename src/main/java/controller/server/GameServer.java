
package controller.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.*;
import game.TypingPlayer;

public class GameServer {

    private static final int SERVER_PORT = 8080;
    private final ServerSocket serverSocket;
    private final List<ConnectionManager> connectionManagers;
    private final List<String> playerNames;
    private final Moshi moshi;

    /**
     * Constructor for GameServer class.
     *
     * @throws IOException for ServerSocket.
     */
    public GameServer() throws IOException {
        this.serverSocket = new ServerSocket(SERVER_PORT);
        this.connectionManagers = new ArrayList<>();
        this.playerNames = new ArrayList<>();
        this.moshi = new Moshi.Builder().build();

        System.out.println("Server started, listening...");
    }

    /**
     * Start Method for server socket.
     */
    public void start() {
        System.out.println("Waiting for clients...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected...");
                ConnectionManager connectionManager = new ConnectionManager(clientSocket, this);
                connectionManagers.add(connectionManager);
                connectionManager.start();
            } catch (IOException e) {
                System.out.println("Error connecting to client!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method broadcast each message.
     *
     * @param message the string received from connection managers.
     */
    public void broadcastMessage(String message) {
        for (ConnectionManager connectionManager : connectionManagers) {
            connectionManager.sendMessage(message);
        }
    }

    /**
     * Method for update the player List.
     */
    public void broadcastPlayerListUpdate() {
        PlayerListUpdateNotification updateNotification = new PlayerListUpdateNotification(playerNames);
        String json = moshi.adapter(PlayerListUpdateNotification.class).toJson(updateNotification);
        broadcastMessage(json);
    }

    /**
     * Method for adding new players in the server.
     *
     * @param playerName id of the player.
     */
    public void addPlayer(String playerName) {
        playerNames.add(playerName);
        broadcastPlayerListUpdate();
        if (playerNames.size() == 6) {
            broadcastLobbyFull();
        }
    }

    /**
     * Method for remove the players from the server.
     *
     * @param playerName id of the player.
     */
    public void removePlayer(String playerName) {
        playerNames.remove(playerName);

        PlayerLeftNotification leftNotification = new PlayerLeftNotification(playerName);
        String json = moshi.adapter(PlayerLeftNotification.class).toJson(leftNotification);
        broadcastMessage(json);
        broadcastPlayerListUpdate();
    }

    /**
     * Method to remind when lobby is full.
     */
    private void broadcastLobbyFull() {
        LobbyFullNotification lobbyFullNotification = new LobbyFullNotification();
        String json = moshi.adapter(LobbyFullNotification.class).toJson(lobbyFullNotification);
        broadcastMessage(json);
    }

    /**
     * StartGame method.
     *
     * After checked that all the players are there
     */
    public void startGame() {
        System.out.println("Starting the game...");
        List<TypingPlayer> players = new ArrayList<>();
        for (String playerName : playerNames) {
            players.add(new TypingPlayer(playerName));
        }
        GameStartNotification gameStartNotification = new GameStartNotification(players, 0); // Assuming the first player is the current player

        String json = moshi.adapter(GameStartNotification.class).toJson(gameStartNotification);
        broadcastMessage(json);
    }

    /**
     * Gets connection managers.
     *
     * @return the connection managers
     */
    public List<ConnectionManager> getConnectionManagers() {
        return connectionManagers;
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
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
