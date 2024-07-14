package network.client;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import communication.messages.GameEndNotification;
import communication.messages.GameStateNotification;
import communication.messages.HostNotification;
import communication.messages.PlayerJoinedNotification;
import communication.messages.PlayerLeftRequest;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.RankingNotification;
import communication.messages.StartGameRequest;
import communication.messages.UpdateRankingRequest;
import controller.client.ClientController;
import controller.client.GameClient;
import game.TypingPlayer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import network.MockSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** The type Client test. */
@Timeout(value = 5)
public class ClientTest {

  @Mock private ClientController clientController;

  private MockSocket mockSocket;
  private GameClient gameClient;

  private ByteArrayOutputStream outputStream;
  private ByteArrayInputStream inputStream;

  /**
   * Sets up.
   *
   * @throws IOException the io exception
   */
  @BeforeEach
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);

    outputStream = new ByteArrayOutputStream();
    inputStream = new ByteArrayInputStream(new byte[0]); // This creates an empty stream

    mockSocket = new MockSocket(inputStream, outputStream);

    gameClient = new GameClient(clientController, "DummyPlayer", mockSocket);
  }

  /**
   * Tear down.
   *
   * @throws IOException the io exception
   */
  @AfterEach
  public void tearDown() throws IOException {
    outputStream.close();
    inputStream.close();
  }

  /**
   * Assert that contains nkey value pairs.
   *
   * @param json the json
   * @param expectedPairs the expected pairs
   * @throws JSONException the json exception
   */
  public void assertThatContainsNkeyValuePairs(String json, int expectedPairs)
      throws JSONException {
    JSONObject jsonObject = new JSONObject(json);
    int count = countPairs(jsonObject);
    assertEquals(
        expectedPairs, count, "The number of key-value pairs does not match the expected value.");
  }

  private int countPairs(JSONObject jsonObject) {
    int count = jsonObject.length();

    for (String key : jsonObject.keySet()) {
      Object value = jsonObject.get(key);
      if (value instanceof JSONObject) {
        count += countPairs((JSONObject) value);
      } else if (value instanceof JSONArray) {
        count += countPairsInArray((JSONArray) value);
      }
    }

    return count;
  }

  private int countPairsInArray(JSONArray array) throws JSONException {
    int count = 0;
    for (int i = 0; i < array.length(); i++) {
      Object item = array.get(i);
      if (item instanceof JSONObject) {
        count += countPairs((JSONObject) item);
      } else if (item instanceof JSONArray) {
        count += countPairsInArray((JSONArray) item);
      } else {
        count++; // Each item in array is counted as a value
      }
    }
    return count;
  }

  @Test
  public void testClient1_sendJoinGameRequest() throws InterruptedException {
    String messageToSend = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"DummyUser\"}";
    gameClient.sendMessage(messageToSend);

    Thread.sleep(500);

    String sentMessages = outputStream.toString();
    sentMessages = sentMessages.replaceAll("\\s+", "");

    assertThat(sentMessages).contains(messageToSend);

    assertThat(sentMessages).matches(".*\"messageType\":\"JoinGameRequest\".*");
    assertThat(sentMessages).matches(".*\"playerName\":\"DummyUser\".*");

    assertThatContainsNkeyValuePairs(sentMessages, 2);
  }

  @Test
  public void testClient2_receivePlayerListUpdateNotification() {
    String jsonNotification =
        "{\"messageType\":\"PlayerListUpdateNotification\",\"playerNames\":[\"Alice\", \"Bob\"]}";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    PlayerListUpdateNotification expectedNotification =
        new PlayerListUpdateNotification(Arrays.asList("Alice", "Bob"));

    verify(clientController, timeout(1000))
        .handlePlayerListUpdate(
            argThat(
                notification ->
                    notification.getPlayerNames().containsAll(Arrays.asList("Alice", "Bob"))
                        && notification.getPlayerNames().size() == 2));
  }

  @Test
  public void testClient3_receivePlayerJoinedNotification() {
    String playerName = "NewPlayer";
    int numPlayers = 5;
    String jsonNotification =
        String.format(
            "{\"messageType\":\"PlayerJoinedNotification\","
                + "\"newPlayerName\":\"%s\",\"numPlayers\":%d}",
            playerName, numPlayers);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    verify(clientController, timeout(1000)).handlePlayerJoined(any(PlayerJoinedNotification.class));
  }

  @Test
  public void testClient4_receiveLobbyFullNotification() {
    String jsonNotification = "{\"messageType\":\"LobbyFullNotification\"}";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    verify(clientController, timeout(1000)).handleLobbyFull();
  }

  @Test
  public void testClient5_sendStartGameRequest() {
    String hostPlayerName = "HostPlayer";
    String providedText = "text";
    StartGameRequest request = new StartGameRequest(hostPlayerName, providedText);
    String messageToSend =
        "{\"messageType\":\"StartGameRequest\",\"hostPlayerName\":\"" + hostPlayerName + "\"}";

    gameClient.sendMessage(messageToSend);

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String sentMessages = outputStream.toString().replaceAll("\\s+", "");

    assertThat(sentMessages).contains(messageToSend);

    assertThat(sentMessages).matches(".*\"messageType\":\"StartGameRequest\".*");
    assertThat(sentMessages).matches(".*\"hostPlayerName\":\"" + hostPlayerName + "\".*");

    assertThatContainsNkeyValuePairs(sentMessages, 2);
  }

  @Test
  public void testClient6_receiveGameStartNotification() {
    String jsonNotification =
        "{\"messageType\":\"GameStartNotification\","
            + "\"players\":[{\"name\":\"Alice\", \"wpm\":0, \"accuracy\":0.0}],"
            + "\"text\":\"Sample game text\"}";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    verify(clientController, timeout(1000))
        .handleGameStart(
            argThat(
                notification ->
                    "GameStartNotification".equals(notification.getMessageType())
                        && notification.getPlayers().size() == 1
                        && "Alice".equals(notification.getPlayers().get(0).getName())
                        && notification.getPlayers().get(0).getWpm() == 0
                        && notification.getPlayers().get(0).getAccuracy() == 0.0
                        && "Sample game text".equals(notification.getText())));
  }

  @Test
  public void testClient7_sendUpdateProgressRequest() {
    String playerName = "DummyUser";
    int wpm = 120;
    int time = 60;
    int progress = 50;
    double accuracy = 95.5;

    String messageToSend =
        String.format(
            "{\"messageType\":\"UpdateProgressRequest\",\"playerName\":\"%s\",\"wpm\":%d,"
                + "\"time\":%d,\"progress\":%d,\"accuracy\":%.1f}",
            playerName, wpm, time, progress, accuracy);

    gameClient.sendMessage(messageToSend);

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String sentMessages = outputStream.toString().replaceAll("\\s+", "");

    assertThat(sentMessages).contains(messageToSend);
    assertThat(sentMessages).matches(".*\"messageType\":\"UpdateProgressRequest\".*");
    assertThat(sentMessages).matches(".*\"playerName\":\"" + playerName + "\".*");
    assertThat(sentMessages).matches(".*\"wpm\":" + wpm + ".*");
    assertThat(sentMessages).matches(".*\"time\":" + time + ".*");
    assertThat(sentMessages).matches(".*\"progress\":" + progress + ".*");
    assertThat(sentMessages).matches(".*\"accuracy\":" + accuracy + ".*");

    assertThatContainsNkeyValuePairs(sentMessages, 6);
  }

  @Test
  public void testClient8_receiveGameStateNotification() {
    String playerName = "DummyPlayer";
    int wpm = 100;
    int time = 30;
    int progress = 50;
    double accuracy = 95.5;
    String jsonNotification =
        String.format(
            "{\"messageType\":\"GameStateNotification\",\"playerName\":\"%s\","
                + "\"wpm\":%d,\"time\":%d,\"progress\":%d,\"accuracy\":%f}",
            playerName, wpm, time, progress, accuracy);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    GameStateNotification expectedNotification =
        new GameStateNotification(playerName, wpm, time, progress, accuracy);
    verify(clientController, timeout(1000))
        .handleProgress(
            argThat(
                notification ->
                    notification.getPlayerName().equals(expectedNotification.getPlayerName())
                        && notification.getWpm() == expectedNotification.getWpm()
                        && notification.getTime() == expectedNotification.getTime()
                        && notification.getProgress() == expectedNotification.getProgress()
                        && notification.getAccuracy() == expectedNotification.getAccuracy()));
  }

  @Test
  public void testClient9_sendPlayerLeftRequest() {
    String playerName = "DummyPlayer";
    PlayerLeftRequest request = new PlayerLeftRequest(playerName);
    String expectedMessage =
        String.format("{\"messageType\":\"PlayerLeftRequest\",\"playerName\":\"%s\"}", playerName);

    gameClient.sendMessage(expectedMessage);

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String sentMessages = outputStream.toString().replaceAll("\\s+", "");

    assertThat(sentMessages).contains(expectedMessage);

    assertThat(sentMessages).matches(".*\"messageType\":\"PlayerLeftRequest\".*");
    assertThat(sentMessages).matches(".*\"playerName\":\"" + playerName + "\".*");

    assertThatContainsNkeyValuePairs(sentMessages, 2);
  }

  @Test
  public void testClient10_receivePlayerLeftNotification() throws InterruptedException {
    String playerName = "DummyPlayer";
    int numPlayers = 4;
    String jsonNotification =
        String.format(
            "{\"messageType\":\"PlayerLeftNotification\",\"playerName\":\"%s\",\"numPlayers\":%d}",
            playerName, numPlayers);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    Thread.sleep(500);

    verify(clientController, timeout(1000))
        .handlePlayerLeft(
            argThat(
                notification ->
                    notification.getPlayerName().equals(playerName)
                        && notification.getNumPlayers() == numPlayers));
  }

  @Test
  public void testClient11_sendEndGameRequest() {
    String playerName = "TestPlayer";
    int time = 300;
    int wpm = 50;
    double accuracy = 97.5;

    String expectedMessage =
        String.format(
            "{\"messageType\":\"EndGameRequest\",\"playerName\":\"%s\","
                + "\"time\":%d,\"wpm\":%d,\"accuracy\":%.1f}",
            playerName, time, wpm, accuracy);

    gameClient.sendMessage(expectedMessage);

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String sentMessages = outputStream.toString().replaceAll("\\s+", "");

    assertThat(sentMessages).contains(expectedMessage);

    assertThat(sentMessages).matches(".*\"messageType\":\"EndGameRequest\".*");
    assertThat(sentMessages).matches(".*\"playerName\":\"" + playerName + "\".*");
    assertThat(sentMessages).matches(".*\"time\":" + time + ".*");
    assertThat(sentMessages).matches(".*\"wpm\":" + wpm + ".*");
    assertThat(sentMessages).matches(".*\"accuracy\":" + accuracy + ".*");

    assertThatContainsNkeyValuePairs(sentMessages, 5);
  }

  @Test
  public void testClient12_receiveGameEndNotification() throws IOException, InterruptedException {
    String playerName = "TestPlayer";
    int wpm = 75;
    int time = 360;
    double accuracy = 92.5;
    String jsonNotification =
        String.format(
            "{\"messageType\":\"GameEndNotification\",\"playerName\":\"%s\","
                + "\"wpm\":%d,\"accuracy\":%.1f,\"time\":%d}",
            playerName, wpm, accuracy, time);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    GameEndNotification expectedNotification =
        new GameEndNotification(playerName, wpm, accuracy, time);

    verify(clientController, timeout(1000))
        .handleGameEnd(
            argThat(
                notification ->
                    notification.getPlayerName().equals(expectedNotification.getPlayerName())
                        && notification.getWpm() == expectedNotification.getWpm()
                        && notification.getTime() == expectedNotification.getTime()
                        && notification.getAccuracy() == expectedNotification.getAccuracy()));
  }

  @Test
  public void testClient13_sendUpdateRankingRequest() {

    TypingPlayer player1 = new TypingPlayer("Alice");
    player1.setWpm(40);
    player1.setAccuracy(95.5);
    player1.setProgress(100);
    player1.setHasFinished(true);

    TypingPlayer player2 = new TypingPlayer("Bob");
    player2.setWpm(30);
    player2.setAccuracy(80.5);
    player2.setProgress(100);
    player2.setHasFinished(true);
    List<TypingPlayer> players = Arrays.asList(player1, player2);

    UpdateRankingRequest request = new UpdateRankingRequest(players);
    String messageToSend =
        "{\"messageType\":\"UpdateRankingRequest\",\"players\":["
            + "{\"name\":\"Alice\",\"wpm\":40,\"accuracy\":95.5,\"progress\":100,"
            + "\"hasFinished\":true},"
            + "{\"name\":\"Bob\",\"wpm\":30,\"accuracy\":80.5,\"progress\":100,"
            + "\"hasFinished\":true}"
            + "]}";

    gameClient.sendMessage(messageToSend);

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    String sentMessages = outputStream.toString().replaceAll("\\s+", "");

    assertThat(sentMessages).contains(messageToSend);

    assertThat(sentMessages).matches(".*\"messageType\":\"UpdateRankingRequest\".*");
    assertThat(sentMessages)
        .matches(
            ".*\"players\":\\[\\{\"name\":\"Alice\",\"wpm\":40,\"accuracy\":95\\.5,"
                + "\"progress\":100,\"hasFinished\":true\\},\\{\"name\":\"Bob\",\"wpm\":30,"
                + "\"accuracy\":80\\.5,\"progress\":100,\"hasFinished\":true\\}\\].*");

    assertThatContainsNkeyValuePairs(sentMessages, 12);
  }

  @Test
  public void testClient14_receiveRankingNotification() {

    String jsonNotification =
        "{\"messageType\":\"RankingNotification\",\"rankings\":["
            + "{\"name\":\"Alice\",\"wpm\":100,\"accuracy\":95.5,\"progress\":100},"
            + "{\"name\":\"Bob\",\"wpm\":80,\"accuracy\":90.0,\"progress\":80}"
            + "]}";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    TypingPlayer player1 = new TypingPlayer("Alice");
    player1.setWpm(100);
    player1.setAccuracy(95.5);
    player1.setProgress(100);

    TypingPlayer player2 = new TypingPlayer("Bob");
    player2.setWpm(80);
    player2.setAccuracy(90.0);
    player2.setProgress(80);
    List<TypingPlayer> expectedRankings = Arrays.asList(player1, player2);

    verify(clientController, timeout(1000))
        .handleRankingNotification(
            argThat(
                notification -> {
                  if (notification.getRankings().size() != expectedRankings.size()) {
                    return false;
                  }
                  for (int i = 0; i < notification.getRankings().size(); i++) {
                    TypingPlayer expected = expectedRankings.get(i);
                    TypingPlayer actual = notification.getRankings().get(i);
                    if (!expected.getName().equals(actual.getName())
                        || expected.getWpm() != actual.getWpm()
                        || expected.getAccuracy() != actual.getAccuracy()
                        || expected.getProgress() != actual.getProgress()) {
                      return false;
                    }
                  }
                  return true;
                }));

    verify(clientController).handleRankingNotification(any(RankingNotification.class));
  }

  @Test
  public void testClient15_receiveHostNotification() {
    String hostName = "HostPlayer";
    String jsonNotification =
        "{\"messageType\":\"HostNotification\",\"host\":\"" + hostName + "\"}";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
    mockSocket.setInputStream(inputStream);

    gameClient.startListening();

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    verify(clientController, timeout(1000))
        .handleHostNotification(argThat(notification -> hostName.equals(notification.getHost())));

    verify(clientController).handleHostNotification(any(HostNotification.class));
  }
}
