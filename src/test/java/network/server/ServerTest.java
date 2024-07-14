package network.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import communication.messages.JoinGameRequest;
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
        setMockServerSocket(server, mockedServerSocket);  // Reflex
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





}
