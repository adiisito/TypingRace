
package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.*;

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
    private final Map<String, Integer> playerResults;

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
        this.playerResults = new HashMap<>();
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

        System.out.println("MessageType in processMessage is " + messageType);

        if (messageType.equals("JoinGameRequest")) {
            System.out.println("Received JoinGameRequest");
            JoinGameRequest joinGameRequest = moshi.adapter(JoinGameRequest.class).fromJson(message);
            server.handleJoinGameRequest(joinGameRequest);

        } else if (messageType.equals("StartGameRequest")) {
            System.out.println("Received StartGameRequest");
            server.startGame();

        } else if (messageType.equals("PlayerLeftRequest")) {
            PlayerLeftRequest leftRequest = moshi.adapter(PlayerLeftRequest.class).fromJson(message);
            System.out.println("Player leaving: " + leftRequest.getPlayerName());
            server.removePlayer(leftRequest.getPlayerName());

        } else if(messageType.equals("UpdateProgressRequest")){
            UpdateProgressRequest updateProgressRequest = moshi.adapter(UpdateProgressRequest.class).fromJson(message);
            handleUpdateProgressRequest(updateProgressRequest);

        } else if (messageType.equals("EndGameRequest")){
            EndGameRequest endGameRequest = moshi.adapter(EndGameRequest.class).fromJson(message);
            handleEndGameRequest(endGameRequest);

        } else if(messageType.equals("UpdateRankingRequest")){
            UpdateRankingRequest rankingRequest = moshi.adapter(UpdateRankingRequest.class).fromJson(message);
            handleUpdateRankingRequest(rankingRequest);

        }
    }

    /**
     * Handle update progress request.
     *
     * @param request which sended from client.
     */
    private void handleUpdateProgressRequest(UpdateProgressRequest request) {
        GameStateNotification notification = new GameStateNotification(request.getPlayerName(), request.getWpm(), request.getTime(), request.getProgress(), request.getAccuracy());
        // 4 parameters were needed here: wpm, name, progress and time.
        String json = moshi.adapter(GameStateNotification.class).toJson(notification);
        server.broadcastMessage(json);
    }

    /**
     * Handle end Game request.
     * server sends back the noti for each player.
     *
     * @param request messages from client.
     */
    public void handleEndGameRequest(EndGameRequest request) {
        this.playerName = request.getPlayerName();
        // server.
        recordPlayerFinished(playerName);

        GameEndNotification notification = new GameEndNotification(request.getPlayerName(), request.getWpm(), request.getAccuracy(), request.getTime());
        String json = moshi.adapter(GameEndNotification.class).toJson(notification);
        server.broadcastMessage(json);
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

    public void handleUpdateRankingRequest(UpdateRankingRequest request) {

        RankingNotification notification = new RankingNotification(request.getPlayers());
        String json = moshi.adapter(RankingNotification.class).toJson(notification);
        server.broadcastMessage(json);

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
