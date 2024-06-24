package controller.client;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;
import communication.messages.Message;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// mock client
public class GameClient {

    private static final String HOSTNAME = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String DEFAULT_USERNAME = System.getProperty("username");

    private final Socket socket;
    private final PrintWriter out;
    private final ClientController clientController;

    private final Moshi moshi;
    private final JsonAdapter<Message> messageAdapter;

    public GameClient(ClientController clientController) throws IOException {
        this.moshi = new Moshi.Builder().build();
        this.messageAdapter = moshi.adapter(Message.class);

        this.clientController = clientController;
        this.socket = new Socket(HOSTNAME,SERVER_PORT);
        this.out = new PrintWriter(socket.getOutputStream(), true);

        new Thread (() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader((socket.getInputStream())))) {
                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.isEmpty()) {
                        processMessage(message);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void processMessage (String message) throws IOException {
        System.out.println("[DEBUG] message from server" + "\n" +
                "        content: " + message);

        Message messageObject = messageAdapter.fromJson(message);
        if (messageObject instanceof JoinGameRequest) {
            clientController.joinGameRequest(DEFAULT_USERNAME);
        }
    }

    public void sendMessage (Message message) {
        String json = messageAdapter.toJson(message);
        out.println(json);
    }
}
