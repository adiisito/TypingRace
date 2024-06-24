package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;
import communication.messages.Message;
import communication.messages.PlayerJoinedNotification;


public class ConnectionManager extends Thread implements Runnable  {
        private final Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private final JsonAdapter<Message> messageAdapter;
        int numPlayers = 0;

        public ConnectionManager(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);

            Moshi moshi = new Moshi.Builder().build();
            this.messageAdapter = moshi.adapter(Message.class);
        }



    @Override
    public void run() {
            try {
                String messageLine;
                while ((messageLine = in.readLine()) != null){
                    System.out.println("receivedd from client...");
                    processMessage(messageLine);
                }
            } catch(IOException exception) {
                System.out.println("error reading client...");
                exception.printStackTrace();

            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }

            }

    }

    private void processMessage(String message) throws IOException{
            Message messageObject = messageAdapter.fromJson(message);
            if (messageObject instanceof JoinGameRequest) {
                handleJoinGameRequest((JoinGameRequest) messageObject);

            }
             // other kinds of messages

    }

    private void handleJoinGameRequest(JoinGameRequest request) {
        System.out.println("Handle join game request for "+ request.getPlayerName());
        numPlayers++;
        // add game logic
        sendMessage(new PlayerJoinedNotification("Welcome, " + request.getPlayerName(), numPlayers));
    }

    public void sendMessage(Message message) {
            String json = messageAdapter.toJson(message);
            out.println(json);
    }
}
