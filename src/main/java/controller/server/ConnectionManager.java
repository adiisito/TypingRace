
package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.*;
import game.Player;
import game.TypingPlayer;

/**
 * The type Connection manager.
 */
public class ConnectionManager extends Thread {

    private final Socket clientSocket;
    private final GameServer server;
    private BufferedReader in;
    private PrintWriter out;
    private final JsonAdapter<MessageType> messageAdapter;
    private final Moshi moshi = new Moshi.Builder().build();
    private String playerName;
    private final List<ConnectionManager> connectionManagers;
    private final List<String> playerNames;
    private final Set<String> finishedPlayers = new HashSet<>();

    /**
     * Instantiates a new Connection manager.
     *
     * @param socket the socket
     * @param server the server
     * @throws IOException the io exception
     */
    public ConnectionManager(Socket socket, GameServer server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.messageAdapter = moshi.adapter(MessageType.class);
        this.connectionManagers = new ArrayList<>();
        this.playerNames = new ArrayList<>();
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        try {
            String messageLine;
            while ((messageLine = in.readLine()) != null) {
                System.out.println("Received from client: " + messageLine);
                connectionManagers.add(this);
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


    /**
     * Method for process messages.
     *
     * @param message messages to client.
     * @throws IOException for Json messages.
     */
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

        } else if (messageType.equals("PlayerLeftRequest")) {
            PlayerLeftNotification leftNotification = moshi.adapter(PlayerLeftNotification.class).fromJson(message);
            server.removePlayer(leftNotification.getPlayerName());

        } else if(messageType.equals("UpdateProgressRequest")){
            UpdateProgressRequest updateProgressRequest = moshi.adapter(UpdateProgressRequest.class).fromJson(message);
            handleUpdateProgressRequest(updateProgressRequest);

        } else if (messageType.equals("EndGameRequest")){
            EndGameRequest endGameRequest = moshi.adapter(EndGameRequest.class).fromJson(message);
            handleEndGameRequest(endGameRequest);
        }
        // @yili and @yuanyuan, please add other notifs according to the need!
    }

    /**
     * Handle join game request.
     *
     * @param request from client
     */
    private void handleJoinGameRequest(JoinGameRequest request) {
        this.playerName = request.getPlayerName();
        System.out.println("Handle join game request for " + playerName);

        PlayerJoinedNotification notification = new PlayerJoinedNotification(playerName, getConnectionManagers().size());
        String json = moshi.adapter(PlayerJoinedNotification.class).toJson(notification);
        server.broadcastMessage(json);

        server.addPlayer(playerName);
    }

    /**
     * Handle update progress request.
     *
     * @param request which sended from client.
     */
    private void handleUpdateProgressRequest(UpdateProgressRequest request) {
        GameStateNotification notification = new GameStateNotification(request.getPlayerName(), request.getProgress(),request.getTime(), request.getWpm());
        // 4 parameters were needed here: wpm, name, progress and time.
        String json = moshi.adapter(GameStateNotification.class).toJson(notification);
        broadcastMessage(json);
    }

    /**
     * Handle end Game request.
     * server sends back the noti for each player.
     *
     * @param request messages from client.
     */
    private void handleEndGameRequest(EndGameRequest request) {
        this.playerName = request.getPlayerName();
        // server.
        recordPlayerFinished(playerName);

        GameEndNotification notification = new GameEndNotification(request.getPlayerName(), request.getTime(), request.getWpm());
        String json = moshi.adapter(GameEndNotification.class).toJson(notification);
        broadcastMessage(json);
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
     * Method to remind when lobby is full.
     */
    private void broadcastLobbyFull() {
        LobbyFullNotification lobbyFullNotification = new LobbyFullNotification();
        String json = moshi.adapter(LobbyFullNotification.class).toJson(lobbyFullNotification);
        broadcastMessage(json);
    }

    /**
     * Method to count the number of players who had finished their games.
     *
     * @param playerName names of them.
     */
    public synchronized void recordPlayerFinished(String playerName){
        finishedPlayers.add(playerName);
        if (finishedPlayers.size() == connectionManagers.size()) {
            broadcastAllPlayersEnded();
        }
    }

    /**
     * Broadcast to send on the finish window.
     * TODO waiting for the implementation of client to send the result
     */
    private void broadcastAllPlayersEnded() {
        /*
        List<TypingPlayer> results = ;
        AllEndedNotification notification = new AllEndedNotification(results);
        String json = moshi.adapter(AllEndedNotification.class).toJson(notification);
        for (ConnectionManager manager : connectionManagers) {
            manager.sendMessage(json);
        }

         */
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
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        System.out.println("Sending JSON: " + message);
        out.println(message);
        out.flush();
    }

    /**
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }




}
