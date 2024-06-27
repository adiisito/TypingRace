
package controller.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.*;
import game.GameState;
import game.TypingPlayer;


public class GameServer {

    private static final int SERVER_PORT = 8080;
    private final ServerSocket serverSocket;
    private final List<ConnectionManager> connectionManagers;


    /**
     * Constructor for GameServer class.
     *
     * @throws IOException for ServerSocket.
     */
    public GameServer() throws IOException {
        this.serverSocket = new ServerSocket(SERVER_PORT);
        this.connectionManagers = new ArrayList<>();

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
