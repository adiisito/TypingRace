package network.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import communication.messages.GameStateNotification;
import communication.messages.JoinGameRequest;
import communication.messages.StartGameRequest;
import communication.messages.UpdateProgressRequest;
import controller.server.ConnectionManager;
import controller.server.GameServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.squareup.moshi.Moshi;

/**
 * The type Server test.
 */
public class ServerTest {
    @Mock
    private ServerSocket mockedServerSocket;
    @Mock
    private Socket mockedSocket;
    @Mock
    private ConnectionManager mockedConnectionManager;

    private GameServer server;
    private AutoCloseable closeable;
    private ConnectionManager manager;
    private BufferedReader in;
    private PrintWriter out;
    private ByteArrayOutputStream outputStream;
    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        when(mockedServerSocket.accept()).thenReturn(mockedSocket);
        // Initializing of the two stream.
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        when(mockedSocket.getOutputStream()).thenReturn(outputStream);
        when(mockedSocket.getInputStream()).thenReturn(inputStream);

        server = new GameServer();  // Constructor
        setMockServerSocket(server, mockedServerSocket);  // Reflection
        server.connectionManagers.add(mockedConnectionManager);

        server.playerNamesList = new ArrayList<>();
        server.moshi = new Moshi.Builder().build();
        setMockServerSocket(server, mockedServerSocket);
        manager = new ConnectionManager(mockedSocket, server);
    }

    /**
     * Set a mock server socket to test the connection with client.
     *
     * @param server
     * @param serverSocket
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void setMockServerSocket(GameServer server, ServerSocket serverSocket) throws NoSuchFieldException, IllegalAccessException {
        Field field = GameServer.class.getDeclaredField("serverSocket");
        field.setAccessible(true);
        field.set(server, serverSocket);
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Test handle join game request adds player.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testHandleJoinGameRequestAddsPlayer() throws IOException {
        String playerName = "newPlayer";
        JoinGameRequest joinRequest = new JoinGameRequest(playerName);

        server.handleJoinGameRequest(joinRequest);

        assertTrue(server.playerNamesList.contains(playerName));
        assertEquals(1, server.playerNamesList.size());
    }
    @Test
    public void testJoinGameRequestHandlingAndNotifications() throws IOException {
        // Simulate there is a join game request.
        JoinGameRequest joinRequest = new JoinGameRequest("newPlayer");

        // Dealing with the request.
        server.handleJoinGameRequest(joinRequest);

        // Check if the player was added correctly.
        assertTrue(server.playerNamesList.contains("newPlayer"), "Player should be added.");


        // Check if the Notis were sent correctly.
        verify(mockedConnectionManager, times(1)).sendMessage(contains("PlayerJoinedNotification"));
        verify(mockedConnectionManager, times(1)).sendMessage(contains("PlayerListUpdateNotification"));

        // If the new Player is the first player, check if we sent the host noti.
        if (server.playerNamesList.size() == 1) {
            verify(mockedConnectionManager, times(1)).sendMessage(contains("HostNotification"));
        }
    }

    /**
     * Test lobby full notification sent.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testLobbyFullNotificationSent() throws IOException {
        // Assume that the lobby will be fulled by 6 players.
        for (int i = 1; i <= 6; i++) {
            JoinGameRequest joinRequest = new JoinGameRequest("Player" + i);
            server.handleJoinGameRequest(joinRequest);
        }

        // Check if the lobbyfullnoti is sent successfully.
        verify(mockedConnectionManager, times(1)).sendMessage(contains("LobbyFullNotification"));
    }

    /**
     * Test start game handling and notification.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testStartGameHandlingAndNotification() throws IOException {
        // Add some players to test.
        for (int i = 1; i <= 3; i++) {
            server.addPlayer("Player" + i);
        }
        server.hostPlayerName = "Player1";  // hostplayer.

        StartGameRequest startRequest = new StartGameRequest("Player1", "Sample text for the game.");
        server.startGame(startRequest);

        // Check if the game start noti is sent successfully.
        verify(mockedConnectionManager, times(1)).sendMessage(argThat(message ->
                message.contains("GameStartNotification") &&
                        message.contains("Sample text for the game.")
        ));
    }

    /**
     * Test update progress handling and notification.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testUpdateProgressHandlingAndNotification() throws IOException {
        // Setup initial conditions
        String playerName = "Player1";
        int wpm = 120;
        int progress = 95;
        double accuracy = 98.5;
        int time = 99;

        // Prepare the test request
        UpdateProgressRequest updateRequest = new UpdateProgressRequest(playerName, wpm, progress, accuracy, time);
        String jsonRequest = server.moshi.adapter(UpdateProgressRequest.class).toJson(updateRequest);

        // Inject the JSON request into the ConnectionManager processing method
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonRequest.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ConnectionManager manager = new ConnectionManager(mockedSocket, server);
        manager.in = reader; // Set the custom BufferedReader

        // Execute the method that processes messages
        manager.run(); // Might need adjustment to directly call processMessage depending on method visibility

        // Verification: Check if the GameStateNotification with expected values was sent
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockedConnectionManager, times(1)).sendMessage(messageCaptor.capture());
        String capturedMessage = messageCaptor.getValue();

        assertTrue(capturedMessage.contains("\"wpm\":" + wpm));
        assertTrue(capturedMessage.contains("\"progress\":" + progress));
        assertTrue(capturedMessage.contains("\"time\":" + time));
        assertTrue(capturedMessage.contains("\"accuracy\":" + accuracy));
        assertTrue(capturedMessage.contains("\"playerName\":\"" + playerName + "\""));
        assertTrue(capturedMessage.contains("GameStateNotification"));
    }





}
