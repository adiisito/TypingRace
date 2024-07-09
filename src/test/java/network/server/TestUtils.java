package network.server;

import controller.server.GameServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.NoSuchElementException;

public class TestUtils {
    static GameServer startServer(ServerSocket serverSocket) throws IOException {
        GameServer server = new GameServer(){
            @Override
            protected ServerSocket createServerSocket() throws IOException {
                return serverSocket;
            }
        };

        new Thread(() -> {
            try {
                server.start();
            } catch (NoSuchElementException e) {
                // expected, because number of incoming clients will be exhausted quickly.
            }
        }).start();
        return server;
    }

    static MockInputStream getNetworkIn(String input) {
        return new MockInputStream(input + System.lineSeparator());
    }

    static ByteArrayOutputStream getNetworkOut() {
        return new ByteArrayOutputStream();
    }

    static boolean areDone(Collection<MockInputStream> inputs) {
        return inputs.stream().allMatch(MockInputStream::isDone);
    }
}
