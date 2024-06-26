package controller.server;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;
import communication.messages.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import communication.messages.JoinGameRequest;
import communication.messages.MessageType;
import game.Game;

public class GameServer {

    private static final int SERVER_PORT = 8080;

    // created and listened the pointed port
    private final ServerSocket serverSocket;
    // initialized for analyse and generate the json messages
    private final Moshi moshi;
    private final JsonAdapter<MessageType> messageAdapter;
    // save players as a list
    private List<ConnectionManager> players = new CopyOnWriteArrayList<>();


    public GameServer() throws IOException{

        this.moshi = new Moshi.Builder().build();
        this.messageAdapter = moshi.adapter(MessageType.class);
        this.serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("Server stated, listening...");
    }

    public void start() {
        System.out.println("Server waiting for client..");
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server and client connected...");
                ConnectionManager connectionManager = new ConnectionManager(clientSocket);
                connectionManager.start();
            } catch (IOException e) {
                System.out.println("!Server Error by connection!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        try {
            GameServer server = new GameServer();
            server.start();

        } catch (IOException e){
            System.out.println("Failed to start the server");
            e.printStackTrace();
        }
    }

    // methods to dealing with player-join and player-leaving.
    public void addPlayers(ConnectionManager cm) {
        players.add(cm);
    }

    public void removePlayers(ConnectionManager cm) {
        players.remove(cm);
    }
}