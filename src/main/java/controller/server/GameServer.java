package controller.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import communication.messages.Message;
import org.json.JSONObject;

public class GameServer {

    private final Moshi moshi;
    private final JsonAdapter<Message> messageAdapter;
    private final List<ConnectionManager> clients = new ArrayList<>();

    public GameServer() {
        this.moshi = new Moshi.Builder().build();
        this.messageAdapter = moshi.adapter(Message.class);
    }

    public void start (int port) {
        try ( ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[Server] Listening on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] New client connected: " + clientSocket);

                ConnectionManager connectionManager = new ConnectionManager(clientSocket, this);
                clients.add(connectionManager);
                connectionManager.run();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processMessage(ConnectionManager source, String message) throws IOException {

        JSONObject response = new JSONObject(message);
        String messageType = response.getString("messageType");

        if (messageType.equals("JoinGameRequest")) {
            // JoinGameRequest joinRequest = messageAdapter.fromJson(response.getString("joinRequest"));
            // handleJoinGameRequest(source, joinRequest);
        }

        // process other messages
    }

    private void handleJoinGameRequest (ConnectionManager source, JoinGameRequest joinRequest) {
        System.out.println("[Server] Player joined: " + joinRequest.getPlayerName());
        source.sendMessage("{\"messageType\":\"JoinGameResponse\",\"status\":\"success\"}");
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start(8080);
    }
}
