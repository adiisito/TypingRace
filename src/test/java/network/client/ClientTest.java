package network.client;

import static com.google.common.truth.Truth.assertThat;

import communication.messages.GameEndNotification;
import communication.messages.GameStartNotification;
import communication.messages.GameStateNotification;
import communication.messages.PlayerJoinedNotification;
import communication.messages.PlayerLeftRequest;
import communication.messages.PlayerListUpdateNotification;
import communication.messages.RankingNotification;
import communication.messages.StartGameRequest;
import communication.messages.UpdateRankingRequest;
import controller.client.ClientController;
import controller.client.GameClient;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import game.TypingPlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import network.MockSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Timeout;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Timeout(value = 5)
public class ClientTest {

    @Mock
    private ClientController clientController;

    private MockSocket mockSocket;
    private GameClient gameClient;

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]); // This creates an empty stream

        mockSocket = new MockSocket(inputStream, outputStream);

        gameClient = new GameClient(clientController, "DummyPlayer", mockSocket);
    }

    @AfterEach
    public void tearDown() throws IOException {
        outputStream.close();
        inputStream.close();
    }

    public void assertThatContainsNKeyValuePairs(String json, int expectedPairs) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int count = countPairs(jsonObject);
        assertEquals(expectedPairs, count, "The number of key-value pairs does not match the expected value.");
    }

    private int countPairs(JSONObject jsonObject) {
        int count = jsonObject.length(); // Directly counts keys at current level

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
    public void testClient1_sendJoinGameRequest() throws IOException, InterruptedException {
        String messageToSend = "{\"messageType\":\"JoinGameRequest\",\"playerName\":\"DummyUser\"}";
        gameClient.sendMessage(messageToSend);

        // Allow some time for the message to be processed
        Thread.sleep(500);

        String sentMessages = outputStream.toString();
        // Remove all whitespace and newline characters for a direct comparison
        sentMessages = sentMessages.replaceAll("\\s+","");

        assertThat(sentMessages).contains(messageToSend);

        // Improved regex that considers possible whitespace within JSON keys and values
        assertThat(sentMessages).matches(".*\"messageType\":\"JoinGameRequest\".*");
        assertThat(sentMessages).matches(".*\"playerName\":\"DummyUser\".*");

        // Function to check if the message contains only two key-value pairs
        assertThatContainsNKeyValuePairs(sentMessages, 2);
    }

    @Test
    public void testClient2_receivePlayerListUpdateNotification() throws IOException {
        // Setup a JSON string that simulates a PlayerListUpdateNotification from the server
        String jsonNotification = "{\"messageType\":\"PlayerListUpdateNotification\",\"playerNames\":[\"Alice\", \"Bob\"]}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        // Start the listening thread
        gameClient.startListening();

        // Sleep briefly to give the listening thread time to process the message
        try {
            Thread.sleep(500);  // Adjust timing as necessary for your environment
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle interrupted exception
        }

        // Prepare the expected notification object
        PlayerListUpdateNotification expectedNotification = new PlayerListUpdateNotification(Arrays.asList("Alice", "Bob"));

        // Verify that the controller handles the player list update correctly
        verify(clientController, timeout(1000)).handlePlayerListUpdate(argThat(notification ->
                notification.getPlayerNames().containsAll(Arrays.asList("Alice", "Bob")) &&
                        notification.getPlayerNames().size() == 2
        ));

    }

    @Test
    public void testClient3_receivePlayerJoinedNotification() throws IOException {
        // Prepare the JSON message as it might be received from the server
        String playerName = "NewPlayer";
        int numPlayers = 5;
        String jsonNotification = String.format("{\"messageType\":\"PlayerJoinedNotification\",\"newPlayerName\":\"%s\",\"numPlayers\":%d}",
                playerName, numPlayers);

        // Change the input stream for the mock socket to simulate receiving the message
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        // Start the listening thread
        gameClient.startListening();

        // Sleep briefly to give the listening thread time to process the message
        try {
            Thread.sleep(500);  // Adjust timing as necessary for your environment
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle interrupted exception
        }

        // Verify that the controller handled the update correctly
        verify(clientController, timeout(1000)).handlePlayerJoined(any(PlayerJoinedNotification.class));

    }

    @Test
    public void testClient4_receiveLobbyFullNotification() throws IOException {
        // Prepare the JSON message as it might be received from the server
        String jsonNotification = "{\"messageType\":\"LobbyFullNotification\"}";

        // Change the input stream for the mock socket to simulate receiving the message
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        // Start the listening thread
        gameClient.startListening();

        // Sleep briefly to give the listening thread time to process the message
        try {
            Thread.sleep(500);  // Adjust timing as necessary for your environment
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle interrupted exception
        }

        // Verify that the controller handled the lobby full notification correctly
        verify(clientController, timeout(1000)).handleLobbyFull();

    }

    @Test
    public void testClient5_sendStartGameRequest() throws IOException, InterruptedException {
        // Create a StartGameRequest with a dummy host player name
        String hostPlayerName = "HostPlayer";
        StartGameRequest request = new StartGameRequest(hostPlayerName);
        String messageToSend = "{\"messageType\":\"StartGameRequest\",\"hostPlayerName\":\"" + hostPlayerName + "\"}";

        // Send the message
        gameClient.sendMessage(messageToSend);

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Retrieve the data sent to the server from the outputStream
        String sentMessages = outputStream.toString().replaceAll("\\s+", "");

        // Assertions to ensure the message was sent as expected
        assertThat(sentMessages).contains(messageToSend);

        // Use regex to check that the message contains the correct messageType and hostPlayerName
        assertThat(sentMessages).matches(".*\"messageType\":\"StartGameRequest\".*");
        assertThat(sentMessages).matches(".*\"hostPlayerName\":\"" + hostPlayerName + "\".*");

        // Function to check if the message contains only two key-value pairs
        assertThatContainsNKeyValuePairs(sentMessages, 2);
    }

    @Test
    public void testClient6_receiveGameStartNotification() throws IOException {
        String jsonNotification = "{\"messageType\":\"GameStartNotification\","
                + "\"players\":[{\"name\":\"Alice\", \"wpm\":0, \"accuracy\":0.0}],"
                + "\"text\":\"Sample game text\"}";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        gameClient.startListening();

        // Set up the mock to perform assertions when handleGameStart is called
        doAnswer(invocation -> {
            GameStartNotification notification = invocation.getArgument(0);
            assertNotNull(notification);
            assertEquals("Sample game text", notification.getText());
            assertEquals(1, notification.getNumPlayers());
            return null;
        }).when(clientController).handleGameStart(any(GameStartNotification.class));



        verify(clientController, timeout(1000)).handleGameStart(any(GameStartNotification.class));

    }

    @Test
    public void testClient7_sendUpdateProgressRequest() throws IOException, InterruptedException {
        // Arrange the request details
        String playerName = "DummyUser";
        int wpm = 120;
        int time = 60;
        int progress = 50;
        double accuracy = 95.5;

        // Construct the JSON message that should be sent
        String messageToSend = String.format("{\"messageType\":\"UpdateProgressRequest\",\"playerName\":\"%s\",\"wpm\":%d,\"time\":%d,\"progress\":%d,\"accuracy\":%.1f}",
                playerName, wpm, time, progress, accuracy);

        // Act by sending the message
        gameClient.sendMessage(messageToSend);

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Retrieve the data sent to the server from the outputStream
        String sentMessages = outputStream.toString().replaceAll("\\s+", "");

        // Assert that the sent message matches what was expected
        assertThat(sentMessages).contains(messageToSend);
        assertThat(sentMessages).matches(".*\"messageType\":\"UpdateProgressRequest\".*");
        assertThat(sentMessages).matches(".*\"playerName\":\"" + playerName + "\".*");
        assertThat(sentMessages).matches(".*\"wpm\":" + wpm + ".*");
        assertThat(sentMessages).matches(".*\"time\":" + time + ".*");
        assertThat(sentMessages).matches(".*\"progress\":" + progress + ".*");
        assertThat(sentMessages).matches(".*\"accuracy\":" + accuracy + ".*");

        // Check that the message contains exactly five key-value pairs
        assertThatContainsNKeyValuePairs(sentMessages, 6);
    }

    @Test
    public void testClient8_receiveGameStateNotification() throws IOException, InterruptedException {
        // Prepare the JSON message as it might be received from the server
        String playerName = "DummyPlayer";
        int wpm = 100;
        int time = 30;
        int progress = 50;
        double accuracy = 95.5;
        String jsonNotification = String.format("{\"messageType\":\"GameStateNotification\",\"playerName\":\"%s\",\"wpm\":%d,\"time\":%d,\"progress\":%d,\"accuracy\":%f}",
                playerName, wpm, time, progress, accuracy);

        // Set the input stream for the mock socket to simulate receiving the message
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        // Start the listening thread in the GameClient
        gameClient.startListening();

        // Give some time for the message to be processed
        Thread.sleep(500);

        // Validate that the handleProgress method was called on the ClientController with the correct data
        GameStateNotification expectedNotification = new GameStateNotification(playerName, wpm, time, progress, accuracy);
        verify(clientController, timeout(1000)).handleProgress(argThat(notification ->
                notification.getPlayerName().equals(expectedNotification.getPlayerName()) &&
                        notification.getWpm() == expectedNotification.getWpm() &&
                        notification.getTime() == expectedNotification.getTime() &&
                        notification.getProgress() == expectedNotification.getProgress() &&
                        notification.getAccuracy() == expectedNotification.getAccuracy()
        ));

    }

    @Test
    public void testClient9_sendPlayerLeftRequest() throws InterruptedException {
        // Arrange: Create the request object with the player's name
        String playerName = "DummyPlayer";
        PlayerLeftRequest request = new PlayerLeftRequest(playerName);
        String expectedMessage = String.format("{\"messageType\":\"PlayerLeftRequest\",\"playerName\":\"%s\"}", playerName);

        // Act: Send the message using the gameClient
        gameClient.sendMessage(expectedMessage);

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Assert: Retrieve the data sent to the server from the outputStream
        String sentMessages = outputStream.toString().replaceAll("\\s+", "");

        // Assertions to ensure the message was sent as expected
        assertThat(sentMessages).contains(expectedMessage);

        // Use regex to check that the message contains the correct messageType and playerName
        assertThat(sentMessages).matches(".*\"messageType\":\"PlayerLeftRequest\".*");
        assertThat(sentMessages).matches(".*\"playerName\":\"" + playerName + "\".*");

        // Function to check if the message contains only two key-value pairs
        assertThatContainsNKeyValuePairs(sentMessages, 2);
    }

    @Test
    public void testClient10_receivePlayerLeftNotification() throws IOException, InterruptedException {
        // Prepare the JSON message as it might be received from the server
        String playerName = "DummyPlayer";
        int numPlayers = 4;
        String jsonNotification = String.format("{\"messageType\":\"PlayerLeftNotification\",\"playerName\":\"%s\",\"numPlayers\":%d}",
                playerName, numPlayers);

        // Set the input stream for the mock socket to simulate receiving the message
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        // Start the listening thread in the GameClient
        gameClient.startListening();

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Assert: Check if the handlePlayerLeft method was called on the ClientController with the correct data
        verify(clientController, timeout(1000)).handlePlayerLeft(argThat(notification ->
                notification.getPlayerName().equals(playerName) &&
                        notification.getNumPlayers() == numPlayers));

    }

    @Test
    public void testClient11_sendEndGameRequest() throws IOException, InterruptedException {
        // Arrange: Create the request details
        String playerName = "TestPlayer";
        int time = 300; // in seconds
        int wpm = 50; // words per minute
        double accuracy = 97.5; // percentage

        // Construct the JSON message that should be sent
        String expectedMessage = String.format("{\"messageType\":\"EndGameRequest\",\"playerName\":\"%s\",\"time\":%d,\"wpm\":%d,\"accuracy\":%.1f}",
                playerName, time, wpm, accuracy);

        // Act: Send the message using the gameClient
        gameClient.sendMessage(expectedMessage);

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Assert: Retrieve the data sent to the server from the outputStream
        String sentMessages = outputStream.toString().replaceAll("\\s+", "");

        // Assertions to ensure the message was sent as expected
        assertThat(sentMessages).contains(expectedMessage);

        // Use regex to check that the message contains the correct messageType and player details
        assertThat(sentMessages).matches(".*\"messageType\":\"EndGameRequest\".*");
        assertThat(sentMessages).matches(".*\"playerName\":\"" + playerName + "\".*");
        assertThat(sentMessages).matches(".*\"time\":" + time + ".*");
        assertThat(sentMessages).matches(".*\"wpm\":" + wpm + ".*");
        assertThat(sentMessages).matches(".*\"accuracy\":" + accuracy + ".*");

        // Function to check if the message contains exactly five key-value pairs
        assertThatContainsNKeyValuePairs(sentMessages, 5);
    }

    @Test
    public void testClient12_receiveGameEndNotification() throws IOException, InterruptedException {
        // Prepare the JSON message as it might be received from the server
        String playerName = "TestPlayer";
        int wpm = 75;
        int time = 360;
        double accuracy = 92.5;
        String jsonNotification = String.format("{\"messageType\":\"GameEndNotification\",\"playerName\":\"%s\",\"wpm\":%d,\"accuracy\":%.1f,\"time\":%d}",
                playerName, wpm, accuracy, time);

        // Set the input stream for the mock socket to simulate receiving the message
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonNotification.getBytes());
        mockSocket.setInputStream(inputStream);

        // Start the listening thread in the GameClient
        gameClient.startListening();

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Construct the expected GameEndNotification object to be passed to the handleGameEnd method
        GameEndNotification expectedNotification = new GameEndNotification(playerName, wpm, accuracy, time);

        // Verify that handleGameEnd is called with the correct notification
        verify(clientController, timeout(1000)).handleGameEnd(argThat(notification ->
                notification.getPlayerName().equals(expectedNotification.getPlayerName()) &&
                        notification.getWpm() == expectedNotification.getWpm() &&
                        notification.getTime() == expectedNotification.getTime() &&
                        notification.getAccuracy() == expectedNotification.getAccuracy()
        ));

    }

    @Test
    public void testClient13_sendUpdateRankingRequest() throws IOException, InterruptedException {

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

        // Create the UpdateRankingRequest with the list of players
        UpdateRankingRequest request = new UpdateRankingRequest(players);
        String messageToSend = "{\"messageType\":\"UpdateRankingRequest\",\"players\":["
                + "{\"name\":\"Alice\",\"wpm\":40,\"accuracy\":95.5,\"progress\":100,\"hasFinished\":true},"
                + "{\"name\":\"Bob\",\"wpm\":30,\"accuracy\":80.5,\"progress\":100,\"hasFinished\":true}"
                + "]}";
        // Act: Send the message using the gameClient
        gameClient.sendMessage(messageToSend);

        // Allow some time for the message to be processed
        Thread.sleep(500);

        // Assert: Retrieve the data sent to the server from the outputStream
        String sentMessages = outputStream.toString().replaceAll("\\s+", "");

        // Assertions to ensure the message was sent as expected
        assertThat(sentMessages).contains(messageToSend);

        // Use regex to check that the message contains the correct messageType and players data
        assertThat(sentMessages).matches(".*\"messageType\":\"UpdateRankingRequest\".*");
        assertThat(sentMessages).matches(".*\"players\":\\[\\{\"name\":\"Alice\",\"wpm\":40,\"accuracy\":95\\.5,\"progress\":100,\"hasFinished\":true\\},\\{\"name\":\"Bob\",\"wpm\":30,\"accuracy\":80\\.5,\"progress\":100,\"hasFinished\":true\\}\\].*");

        // Function to check if the message contains only two key-value pairs
        assertThatContainsNKeyValuePairs(sentMessages, 12);
    }

}
