package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;
import communication.messages.MessageType;
import communication.messages.PlayerJoinedNotification;


public class ConnectionManager extends Thread implements Runnable  {
        private final Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private final JsonAdapter<MessageType> messageAdapter;
        int numPlayers = 0;

        private final Moshi moshi = new Moshi.Builder().build();

        public ConnectionManager(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);

            this.messageAdapter = moshi.adapter(MessageType.class);
        }



    @Override
    public void run() {
            try {
                String messageLine;
                while ((messageLine = in.readLine()) != null){
                    System.out.println("received from client...");
                    System.out.println(messageLine);
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
            MessageType messageObject = moshi.adapter(MessageType.class).fromJson(message);
            String messageType = messageObject.getMessageType();

            if (messageType.equals("JoinGameRequest")) {
                System.out.println("Received JoinGameRequest");
                JoinGameRequest joinGameRequest = moshi.adapter(JoinGameRequest.class).fromJson(message);
                handleJoinGameRequest(joinGameRequest);
            }
             // other kinds of messages

    }

    private void handleJoinGameRequest(JoinGameRequest request) {
        System.out.println("Handle join game request for "+ request.getPlayerName());
        numPlayers++;
        // add game logic

        PlayerJoinedNotification notification = new PlayerJoinedNotification(request.getPlayerName(), numPlayers);
        String json = moshi.adapter(PlayerJoinedNotification.class).toJson(notification);
        sendMessage(json);
    }

    public void sendMessage (String message) {
        System.out.println("Sending JSON: " + message); // Log the JSON being sent
        out.println(message);
        out.flush();
    }
}
