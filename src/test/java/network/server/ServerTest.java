package network.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.squareup.moshi.Moshi;
import communication.messages.JoinGameRequest;
import communication.messages.PlayerJoinedNotification;
import communication.messages.PlayerListUpdateNotification;
import controller.server.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.server.GameServer;
import network.MockSocket;

public class ServerTest {
    private GameServer server;
    private Moshi moshi;

    @BeforeEach
    public void setUp() throws IOException {
        server = new GameServer();
        moshi = new Moshi.Builder().build();
    }

    @Test
    public void testHandleJoinGameRequest() throws IOException, InterruptedException {
        String joinRequestJson = moshi.adapter(JoinGameRequest.class).toJson(new JoinGameRequest("Alice"));
        MockInputStream mockInput = TestUtils.getNetworkIn(joinRequestJson);
        ByteArrayOutputStream mockOutput = TestUtils.getNetworkOut();
        MockSocket mockSocket = new MockSocket(mockInput, mockOutput);
        MockServerSocket mockServerSocket = new MockServerSocket(List.of(mockSocket));

        server = TestUtils.startServer(mockServerSocket);

        ConnectionManager connectionManager = new ConnectionManager(mockSocket, server);
        Thread connectionThread = new Thread(connectionManager::run);
        connectionThread.start();

        // 读取并处理请求
        do {
            Thread.sleep(10);
        } while (!mockInput.isDone());
        Thread.sleep(Sleeps.SLEEP_BEFORE_TESTING);

        String sentMessages = mockOutput.toString();
        System.out.println("Sent messages: " + sentMessages);  // 添加调试输出

        // 验证PlayerListUpdateNotification
        if (!sentMessages.contains("\"messageType\":\"PlayerListUpdateNotification\"")) {
            fail("Expected PlayerListUpdateNotification not found in server output.");
        }
        assertTrue(sentMessages.contains("\"playerNames\":[\"Alice\"]"));
        PlayerListUpdateNotification notification = moshi.adapter(PlayerListUpdateNotification.class).fromJson(sentMessages);
        assertNotNull(notification);
        assertTrue(notification.getPlayerNames().contains("Alice"));

        // 停止连接线程
        connectionThread.interrupt();
    }
}
