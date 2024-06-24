package controller.server;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;
import communication.messages.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private static final int SERVER_PORT = 8080;

    // created and listened the pointed port
    private final ServerSocket serverSocket;
    // initialized for analyse and generate the json messages
    private final Moshi moshi;
    private final JsonAdapter<MessageType> messageAdapter;

    public GameServer() throws IOException{

        this.moshi = new Moshi.Builder().build();
        this.messageAdapter = moshi.adapter(MessageType.class);
        this.serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("Server stated, listening...");
    }

    public void start() {
        System.out.println("waiting for client..");
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected...");
                ConnectionManager connectionManager = new ConnectionManager(clientSocket);
                connectionManager.start();
            } catch (IOException e) {
                System.out.println("!error by connection!");
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
}