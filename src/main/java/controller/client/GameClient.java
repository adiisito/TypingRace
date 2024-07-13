package controller.client;

import com.squareup.moshi.Moshi;
import communication.messages.GameEndNotification;
import communication.messages.GameStartNotification;
import communication.messages.GameStateNotification;
import communication.messages.HostNotification;
import communication.messages.MessageType;
import communication.messages.PlayerLeftNotification;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.RankingNotification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

/**
 * The GameClient class handles the client's connection to the game server. It is responsible for
 * sending and receiving messages, processing server responses, and interacting with the
 * ClientController.
 */
public class GameClient {

  private static final String HOSTNAME = "localhost";
  private static final int SERVER_PORT = 8080;

  private final Socket socket;
  private final PrintWriter out;
  private final ClientController clientController;

  private final Moshi moshi;

  private String playerName;

  /**
   * Constructs a GameClient and establishes a connection to the game server.
   *
   * @param clientController the controller that manages client-side logic
   * @param playerName the name of the player
   * @throws IOException if an I/O error occurs when creating the socket
   */
  public GameClient(ClientController clientController, String playerName, String serverIP)
      throws IOException {
    this.moshi = new Moshi.Builder().build();

    this.clientController = clientController;

    this.playerName = playerName;

    try {
      this.socket = new Socket(serverIP, SERVER_PORT);
      this.out = new PrintWriter(socket.getOutputStream(), true);
      startListening();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Error connecting to the server at " + serverIP + ": " + e.getMessage(),
          "Connection Error",
          JOptionPane.ERROR_MESSAGE);
      throw e;
    }
  }

  /** Starts a new thread to listen for messages from the server. */
  private void startListening() {
    new Thread(
            () -> {
              try (BufferedReader in =
                  new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                  if (!message.isEmpty()) {
                    processMessage(message);
                  }
                }
              } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error while listening to server messages: " + e.getMessage(),
                    "Network Error",
                    JOptionPane.ERROR_MESSAGE);
              } finally {
                try {
                  socket.close();
                } catch (IOException e) {
                  JOptionPane.showMessageDialog(
                      null,
                      "Error closing socket: " + e.getMessage(),
                      "Socket Error",
                      JOptionPane.ERROR_MESSAGE);
                }
              }
            })
        .start();
  }

  /**
   * Processes messages received from the server.
   *
   * @param message the JSON string received from the server
   * @throws IOException if an error occurs while processing the message
   */
  public void processMessage(String message) throws IOException {
    MessageType messageObject = moshi.adapter(MessageType.class).fromJson(message);
    String messageType = messageObject.getMessageType();

    System.out.println("received messageType: " + messageType);

    if (messageType.equals("PlayerListUpdateNotification")) {
      PlayerListUpdateNotification updateNotification =
          moshi.adapter(PlayerListUpdateNotification.class).fromJson(message);
      clientController.handlePlayerListUpdate(updateNotification);
    } else if (messageType.equals("GameStartNotification")) {
      GameStartNotification gameStartNotification =
          moshi.adapter(GameStartNotification.class).fromJson(message);
      clientController.handleGameStart(gameStartNotification);
    } else if (messageType.equals("LobbyFullNotification")) {
      clientController.handleLobbyFull();
    } else if (messageType.equals("PlayerLeftNotification")) {
      PlayerLeftNotification leftNotification =
          moshi.adapter(PlayerLeftNotification.class).fromJson(message);
      clientController.handlePlayerLeft(leftNotification);
    } else if (messageType.equals("GameStateNotification")) {
      GameStateNotification stateNotification =
          moshi.adapter(GameStateNotification.class).fromJson(message);
      clientController.handleProgress(stateNotification);
    } else if (messageType.equals("GameEndNotification")) {
      GameEndNotification gameEndNotification =
          moshi.adapter(GameEndNotification.class).fromJson(message);
      clientController.handleGameEnd(gameEndNotification);
    } else if (messageType.equals("RankingNotification")) {
      RankingNotification rankingNotification =
          moshi.adapter(RankingNotification.class).fromJson(message);
      clientController.handleRankingNotification(rankingNotification);
    } else if (messageType.equals("HostNotification")) {
      HostNotification hostNotification = moshi.adapter(HostNotification.class).fromJson(message);
      clientController.handleHostNotification(hostNotification);
    }
  }

  /**
   * Sends a message to the server.
   *
   * @param message the JSON string to be sent
   */
  public void sendMessage(String message) {
    System.out.println("Sending JSON: " + message);
    out.println(message);
    out.flush();
  }

  /**
   * Gets the player's name.
   *
   * @return the current player's name
   */
  public String getPlayerName() {

    return clientController.getCurrentPlayerName();
  }
}
