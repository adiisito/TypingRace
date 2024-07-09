
package controller.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.*;
import game.GameState;
import game.Text;
import game.TypingPlayer;


public class GameServer {

    private static final int SERVER_PORT = 12345;
    private final ServerSocket serverSocket;
    private final List<ConnectionManager> connectionManagers;
    private List<String> playerNamesList;
            //= new ArrayList<>();
    private final Moshi moshi;

    private String providedText;
    private String hostPlayerName;

    /**
     * Constructor for GameServer class.
     *
     * @throws IOException for ServerSocket.
     */
    public GameServer() throws IOException {
        this.serverSocket = new ServerSocket(SERVER_PORT, 50, InetAddress.getByName("0.0.0.0"));
        this.connectionManagers = new ArrayList<>();
        this.playerNamesList = new ArrayList<>();
        this.moshi = new Moshi.Builder().build();



        System.out.println("Server started, listening...");
    }

    /**
     * StartGame method.
     *
     * After checked that all the players are there
     */
    public void startGame(StartGameRequest request) {
        if (request.getHostPlayerName().equals(hostPlayerName)) {
            System.out.println("Starting the game...");
            List<TypingPlayer> players = new ArrayList<>();
            for (String playerName : playerNamesList) {
                players.add(new TypingPlayer(playerName));
            }
            this.providedText = Text.getRandomText();
            GameStartNotification gameStartNotification = new GameStartNotification(players, providedText);

            String json = moshi.adapter(GameStartNotification.class).toJson(gameStartNotification);
            broadcastMessage(json);
        }
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
        PlayerListUpdateNotification updateNotification = new PlayerListUpdateNotification(new ArrayList<>(playerNamesList));
        String json = moshi.adapter(PlayerListUpdateNotification.class).toJson(updateNotification);
        broadcastMessage(json);
    }


    /**
     * Method for adding new players in the server.
     *
     * @param playerName id of the player.
*/
    public synchronized void addPlayer(String playerName) {
        if (!playerNamesList.contains(playerName)) {
            playerNamesList.add(playerName);
            System.out.println("Player added: " + playerName);

            if (playerNamesList.size() == 1) {
                hostPlayerName = playerName;
                System.out.println("Host set: " + hostPlayerName);
            }

        }else {
            System.out.println("Player rejoined: " + playerName);
        }
        broadcastPlayerListUpdate();
        if (playerNamesList.size() == 6) {
            broadcastLobbyFull();
        }
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
     * Method for remove the players from the server.
     *
     * @param name id of the player.
     */
    public void removePlayer(String name) {
        boolean wasHost = name.equals(hostPlayerName);
        playerNamesList.remove(name);

        int numPlayer = playerNamesList.size();

        PlayerLeftNotification leftNotification = new PlayerLeftNotification(name, numPlayer);
        String json = moshi.adapter(PlayerLeftNotification.class).toJson(leftNotification);
        broadcastMessage(json);

        if (wasHost) {
            if (!playerNamesList.isEmpty()) {
                hostPlayerName = playerNamesList.get(0); // Set new host
                HostNotification hostNotification = new HostNotification(hostPlayerName);
                json = moshi.adapter(HostNotification.class).toJson(hostNotification);
                broadcastMessage(json);
                System.out.println("New host assigned: " + hostPlayerName);
            } else {
                hostPlayerName = null; // No players left to be host
            }
        }

        broadcastPlayerListUpdate();
        System.out.println("Player " + name + " has left the game.");
    }

    /**
     * Handle join game request.
     *
     * @param request from client
     */
    synchronized void handleJoinGameRequest(JoinGameRequest request) {
        // this.playerName = request.getPlayerName();
        System.out.println("Handle join game request for " + request.getPlayerName());
        addPlayer(request.getPlayerName());

        PlayerJoinedNotification notification = new PlayerJoinedNotification(request.getPlayerName(), playerNamesList.size());
        String json = moshi.adapter(PlayerJoinedNotification.class).toJson(notification);
        broadcastMessage(json);

        if (request.getPlayerName().equals(hostPlayerName)) {
            HostNotification hostNotification = new HostNotification(hostPlayerName);
            String json2 = moshi.adapter(HostNotification.class).toJson(hostNotification);
            broadcastMessage(json2);
        }
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
