package network.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.server.GameServer;
import network.MockSocket;

public class ServerTest {
    private GameServer server;
    private MockSocket mockSocket;
    private ByteArrayInputStream mockInput;
    private ByteArrayOutputStream mockOutput;

    @BeforeEach
    public void setup() throws IOException {
        mockInput = new ByteArrayInputStream("Messages from Client".getBytes());
        mockOutput = new ByteArrayOutputStream();
        mockSocket = new MockSocket(mockInput, mockOutput);
        server = new GameServer(); // 假设这是处理连接的服务器
    }

    @Test
    public void testHandlesJoinRequest() throws IOException {
        String joinRequest = "{\"type\":\"JoinGame\",\"playerName\":\"Alice\"}";
        //mockInput.reset(handleJoinGameRequest.getBytes());

        //server.handleJoinGameRequest();

        String response = new String(mockOutput.toByteArray());
        assertTrue(response.contains("Welcome Alice"));
    }
}
