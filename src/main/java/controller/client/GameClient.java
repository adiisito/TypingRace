package controller.client;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// mock client
public class GameClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String DEFAULT_USERNAME = System.getProperty("username");

    private final Moshi moshi;
    private final JsonAdapter<JoinGameRequest> joinAdapter;


    public GameClient() {
        this.moshi = new Moshi.Builder().build();
        this.joinAdapter = moshi.adapter(JoinGameRequest.class);
    }

    public void start() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JoinGameRequest joinRequest = new JoinGameRequest(DEFAULT_USERNAME);
            String joinRequestJson = joinAdapter.toJson(joinRequest);
            out.println(joinRequestJson);

            String response = in.readLine();
            while (!response.isEmpty()) {

                JSONObject responseJson = new JSONObject(response);
                String messageType = responseJson.getString("messageType");

                System.out.println("[MockClient] Received: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GameClient client = new GameClient();
        client.start();
    }
}
