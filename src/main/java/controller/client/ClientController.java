package controller.client;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import communication.messages.EndGameRequest;
import communication.messages.GameEndNotification;
import communication.messages.GameStartNotification;
import communication.messages.GameStateNotification;
import communication.messages.HostNotification;
import communication.messages.JoinGameRequest;
import communication.messages.PlayerJoinedNotification;
import communication.messages.PlayerLeftNotification;
import communication.messages.PlayerLeftRequest;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.RankingNotification;
import communication.messages.StartGameRequest;
import communication.messages.UpdateProgressRequest;
import communication.messages.UpdateRankingRequest;
import game.GameState;
import game.Player;
import game.Text;
import game.TypingPlayer;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.ClientWindow;
import view.GameScreen;
import view.Gui;
import view.ResultScreen;

/** The type Client controller. */
public class ClientController {

  private final Moshi moshi;
  private final GameState gameState;
  private GameClient clientModel;
  private String hostPlayer = null;
  private List<String> playerNames;
  private List<TypingPlayer> players;
  private GameScreen view;
  private ClientWindow clientWindow;
  private Gui mainGui;
  private TypingPlayer currentPlayer;
  private ResultScreen resultScreen;
  private String providedText;
  private String serverIp;
  private boolean soundEnabled = true;
  private String textType = "Random"; // Default category, which we have in settings
  private int numPlayers;

  /**
   * Constructs a new ClientController instance with a new game state and initializes JSON adapter
   * settings.
   */
  public ClientController() {
    this.moshi =
        new Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Player.class, "type")
                    .withSubtype(TypingPlayer.class, "typing"))
            .build();
    this.gameState = new GameState();
  }

  /**
   * Sets the client window associated with this controller.
   *
   * @param clientWindow the ClientWindow to be used with this controller.
   */
  public void setClientWindow(ClientWindow clientWindow) {
    this.clientWindow = clientWindow;
  }

  /**
   * Sets the result screen associated with this controller.
   *
   * @param resultScreen the ResultScreen to be used with this controller.
   */
  public void setResultScreen(ResultScreen resultScreen) {
    this.resultScreen = resultScreen;
  }

  /**
   * Sets the game view associated with this controller.
   *
   * @param view the GameScreen to be controlled.
   */
  public void setView(GameScreen view) {
    this.view = view;
  }

  /**
   * Attempts to join a game with the specified player name.
   *
   * @param playerName the name of player
   * @param serverIp IP address of server
   * @throws IOException if there is an issue sending the join game request over the network
   */
  public void joinGame(String playerName, String serverIp) throws IOException {
    this.serverIp = serverIp;
    if (currentPlayer == null) {
      this.clientModel = new GameClient(this, playerName, this.serverIp);
    }
    JoinGameRequest joinRequest = new JoinGameRequest(playerName);
    String json = moshi.adapter(JoinGameRequest.class).toJson(joinRequest);
    clientModel.sendMessage(json);
    System.out.println("Welcome " + playerName + ". You joined the game at " + serverIp);

    this.currentPlayer = new TypingPlayer(playerName);
  }

  /**
   * Handles updates to the list of players in the game, updating the client window and GUI
   * accordingly.
   *
   * @param updateNotification the notification containing the update list of players
   */
  public void handlePlayerListUpdate(PlayerListUpdateNotification updateNotification) {
    this.playerNames = updateNotification.getPlayerNames();
    clientWindow.updatePlayerList(playerNames);
    System.out.println("Updated player list in client: " + playerNames);
    mainGui.updateAllClientWindows(playerNames);
  }

  /**
   * Handles the event of a player joining the game. This method updates the internal count of the
   * number of players based on the information provided in the player joined notification.
   *
   * @param notification The notification object containing details about the event, including the
   *     number of players currently in the game.
   */
  public void handlePlayerJoined(PlayerJoinedNotification notification) {
    this.numPlayers = notification.getNumPlayers();
  }

  /**
   * Handle game start notification, initializing game state and updating the UI to reflect the game
   * start.
   *
   * @param gameStartNotification the game start notification
   */
  public void handleGameStart(GameStartNotification gameStartNotification) {
    this.players = gameStartNotification.getPlayers();
    this.providedText = gameStartNotification.getText();
    gameState.startNewRace();

    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getName().equals(this.clientModel.getPlayerName())) {
        this.currentPlayer = players.get(i);
        break;
      }
    }

    this.gameState.setPlayers(players);
    this.view = new GameScreen(this.gameState, currentPlayer, this, providedText, soundEnabled);
    this.gameState.startNewRace();
    this.view.addCars();
    SwingUtilities.invokeLater(
        () -> {
          clientWindow.setContentPane(view);
          clientWindow.revalidate();
          clientWindow.repaint();
        });
    System.out.println("Game started with " + players.size() + " players.");
  }

  /**
   * Resets and starts a new game session for the specified player. This method disposes the current
   * game window and creates a new one using the same ClientController, ensuring the game state
   * continuity.
   *
   * @param playerName the name of the player to restart the game for.
   * @throws RuntimeException if an I/O error occurs during the game reinitialization.
   */
  public void startNewGame(String playerName) {
    SwingUtilities.invokeLater(
        () -> {
          try {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(resultScreen);
            frame.setVisible(false);
            frame = new ClientWindow(currentPlayer.getName(), this);
            frame.revalidate();
            frame.repaint();

            joinGame(playerName, serverIp);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /** this method takes to the main menu(LoginWindow). */
  public void toMainMenu() {
    SwingUtilities.invokeLater(
        () -> {
          JFrame frame;
          if (resultScreen != null) {
            frame = (JFrame) SwingUtilities.getWindowAncestor(resultScreen);
            resultScreen = null;
          } else {
            frame = clientWindow;
          }
          frame.setVisible(false);
          Gui newGui = new Gui(this);
          frame = newGui;
          setMainGui(newGui);
          frame.revalidate();
          frame.repaint();
          frame.setVisible(true);
        });
  }

  /**
   * Sends a player left request for the specified player.
   *
   * @param playerName the player name
   */
  public void playerLeft(String playerName) {
    PlayerLeftRequest request = new PlayerLeftRequest(playerName);
    String json = moshi.adapter(PlayerLeftRequest.class).toJson(request);

    clientModel.sendMessage(json);
    System.out.println("Player " + playerName + " is leaving the game.");
  }

  /**
   * Handle player left notification, updating the UI to reflect this change.
   *
   * @param leftNotification the left notification
   */
  public void handlePlayerLeft(PlayerLeftNotification leftNotification) {
    SwingUtilities.invokeLater(
        () -> {
          String playerName = leftNotification.getPlayerName();
          // clientWindow.showPlayerLeftMessage(playerName);
          if (mainGui != null) {
            mainGui.updateAllClientWindowsWithPlayerLeft(playerName);
          }
        });
  }

  /**
   * Start the game.
   *
   * @param playerName the player name
   */
  public void startGame(String playerName) {
    String providedText = Text.getRandomTextByCategory(textType);
    StartGameRequest request = new StartGameRequest(playerName, providedText);
    String json = moshi.adapter(StartGameRequest.class).toJson(request);

    clientModel.sendMessage(json);
    System.out.println("Game started");
  }

  /**
   * Processes a host notification to update the internal reference to the current game host. This
   * method captures the host player's name from the notification and updates the local state to
   * reflect the new host.
   *
   * @param hostNotification The notification object that contains the host player's name.
   */
  public void handleHostNotification(HostNotification hostNotification) {
    hostPlayer = hostNotification.getHost();
  }

  /**
   * Checks if the specified player is the host of the current game. This is determined by comparing
   * the given player name with the stored host player's name.
   *
   * @param playerName The name of the player to check against the current host.
   * @return true if the specified player is the host; false otherwise.
   */
  public boolean isHost(String playerName) {
    return playerName.equals(hostPlayer);
  }

  /**
   * Sends an update of the player's progress to the server.
   *
   * @param playerName the player name
   * @param wpm word per minute rate
   * @param progress current progress
   * @param accuracy current accuracy
   * @param time current time elapsed in the game
   */
  public void updateProgress(String playerName, int wpm, int progress, double accuracy, int time) {
    UpdateProgressRequest progressRequest =
        new UpdateProgressRequest(currentPlayer.getName(), wpm, progress, accuracy, time);
    String json = moshi.adapter(UpdateProgressRequest.class).toJson(progressRequest);
    clientModel.sendMessage(json);
  }

  /**
   * Handles updates to the game state received from server.
   *
   * @param notification game state notification
   */
  public void handleProgress(GameStateNotification notification) {

    // view.updateCarPositions(notification.getPlayerName(),
    // notification.getProgress(), notification.getWpm());
    SwingUtilities.invokeLater(
        () -> {
          if (view != null) {
            Player player = findPlayerByName(notification.getPlayerName());

            if (player != null) {
              // update the game screen with the latest progress, wpm, and accuracy
              currentPlayer.setProgress(notification.getProgress());
              currentPlayer.setWpm(notification.getWpm());
              currentPlayer.setAccuracy(notification.getAccuracy());
              view.updateCarPositions(
                  notification.getPlayerName(), notification.getProgress(), notification.getWpm());

              if (notification.getPlayerName().equals(currentPlayer.getName())) {
                view.updateProgressDisplay();
              }
            }
          }
        });
  }

  /**
   * Finds a player object based on the given player name.
   *
   * @param playerName the name of the player to search for
   * @return the Player object with the specified name, or null if no such player exists
   */
  public Player findPlayerByName(String playerName) {
    for (Player player : gameState.getPlayers()) {
      if (player.getName().equals(playerName)) {
        return player;
      }
    }
    return null;
  }

  /**
   * Sends a request to end the game for the player with the end status.
   *
   * @param playerName player name
   * @param time the total time to finish the game
   * @param wpm words per minute rate
   * @param accuracy the end accuracy
   */
  public void endGame(String playerName, int time, int wpm, double accuracy) {

    EndGameRequest request = new EndGameRequest(playerName, time, wpm, accuracy);
    String json = moshi.adapter(EndGameRequest.class).toJson(request);
    clientModel.sendMessage(json);
    gameState.endCurrentRace();
  }

  /**
   * Handles the notification of the end of the game.
   *
   * @param notification game end notification
   */
  public void handleGameEnd(GameEndNotification notification) {
    gameState.setPlayers(players);

    for (Player player : players) {
      if (player.getName().equals(notification.getPlayerName())) {
        player.setWpm(notification.getWpm());
        player.setAccuracy(notification.getAccuracy());
        player.setHasFinished(true);
      }
    }

    if (notification.getPlayerName().equals(currentPlayer.getName())) {
      currentPlayer.setWpm(notification.getWpm());
      currentPlayer.setAccuracy(notification.getAccuracy());
      currentPlayer.setHasFinished(true);

      SwingUtilities.invokeLater(
          () -> {
            // update result screen
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            if (frame != null) {
              this.resultScreen =
                  new ResultScreen(
                      gameState,
                      currentPlayer,
                      notification.getWpm(),
                      notification.getAccuracy(),
                      Math.toIntExact(notification.getTime()),
                      view.getCarPanel(),
                      view.getWrongChars(),
                      this);
              frame.setContentPane(this.resultScreen);
              frame.revalidate();
              frame.repaint();
              List<TypingPlayer> rankings =
                  resultScreen.computeRankings(gameState.getCompletedPlayers());
              updateRanking(rankings);
            }
          });

      System.out.println(currentPlayer.getName() + ", Game ended.");
    }
  }

  /**
   * Updates the ranking information on the server.
   *
   * @param rankings a list of TypingPlayer objects representing the updated rankings
   */
  public void updateRanking(List<TypingPlayer> rankings) {
    UpdateRankingRequest request = new UpdateRankingRequest(rankings);
    String json = moshi.adapter(UpdateRankingRequest.class).toJson(request);
    clientModel.sendMessage(json);
  }

  /**
   * Handles a notification containing updated ranking information. Updates the ranking table on the
   * result screen if it exists.
   *
   * @param notification a RankingNotification object containing the updated rankings
   */
  public void handleRankingNotification(RankingNotification notification) {
    if (resultScreen != null) {
      SwingUtilities.invokeLater(
          () -> {
            resultScreen.updateRankingTable(notification.getRankings());
          });
    }
  }

  /** Displays the lobby full button on the client window. */
  public void handleLobbyFull() {
    SwingUtilities.invokeLater(() -> clientWindow.updateLobbyStatus());
  }

  /**
   * Retrieves the name of the current player.
   *
   * @return the name of the current player
   */
  public String getCurrentPlayerName() {
    return currentPlayer.getName();
  }

  /**
   * Retrieves the main GUI instance.
   *
   * @return the main GUI instance
   */
  public Gui getMainGui() {
    return mainGui;
  }

  /**
   * Sets the main GUI associated with this controller.
   *
   * @param mainGui the GUI to be associated with this controller.
   */
  public void setMainGui(Gui mainGui) {
    this.mainGui = mainGui;
  }

  /**
   * Checks if the sound is enabled in the application.
   *
   * @return true if sound is enabled, otherwise false.
   */
  public boolean isSoundEnabled() {
    return soundEnabled;
  }

  /**
   * Sets the sound enabled state in the application.
   *
   * @param soundEnabled A boolean value where true enables sound and false disables it.
   */
  public void setSoundEnabled(boolean soundEnabled) {
    this.soundEnabled = soundEnabled;
  }

  /**
   * Retrieves the type of text used in the application. This can refer to different categories of
   * text such as 'Random', 'Fixed', etc., depending on the context.
   *
   * @return A String representing the type of text.
   */
  public String getTextType() {
    return textType;
  }

  /**
   * Sets the type of text to be used in the application. This method allows the specification of
   * text categories such as 'Random', 'Fixed', etc.
   *
   * @param textType The type of text as a String.
   */
  public void setTextType(String textType) {
    this.textType = textType;
  }

  /**
   * Gives a list of players in the round.
   *
   * @return the player list
   */
  public List<TypingPlayer> getPlayers() {
    return players;
  }
}
