package controller.server;

import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager implements Runnable {

    private final Socket clientSocket;
    private final GameServer server;
    private PrintWriter out;
    private BufferedReader in;
    private final Moshi moshi;

    public ConnectionManager(Socket clientSocket, GameServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.moshi = new Moshi.Builder().build();
    }


    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String fromClient = in.readLine();
            while (fromClient != null) {
                server.processMessage(this, fromClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
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

    public void sendMessage(String message) {
        out.println(message);
    }
}
