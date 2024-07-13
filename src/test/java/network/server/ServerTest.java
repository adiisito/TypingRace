package network.server;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ServerTest {
/*
    @Mock
    private ServerSocket serverSocket;
    @Mock
    private Socket clientSocket;

    private GameServer gameServer;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        gameServer = new GameServer(serverSocket);
    }

    @Test
    public void testAcceptClient() throws IOException {
        when(serverSocket.accept()).thenReturn(clientSocket);
        Socket returnedSocket = gameServer.acceptClient();
        verify(serverSocket).accept();  // 验证是否调用了accept方法
        assertSame(clientSocket, returnedSocket);  // 验证返回的Socket是否是预期的Socket
    }

    @Test
    public void testProcessClient() throws IOException {
        doNothing().when(serverSocket).close();  // 确保在测试中不关闭serverSocket
        when(serverSocket.accept()).thenReturn(clientSocket).thenReturn(null);  // 模拟接受一个客户端然后返回null结束循环
        new Thread(() -> {
            try {
                gameServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(100);  // 稍等片刻让服务器运行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameServer.stop();  // 停止服务器循环
        verify(serverSocket, atLeastOnce()).accept();  // 验证至少调用一次accept方法
        verify(clientSocket, atLeastOnce()).getInputStream();  // 如果processClient读取了输入流，这里可以验证
    }

 */
}
