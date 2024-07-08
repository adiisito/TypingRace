package network.client;

import static com.google.common.truth.Truth.assertThat;

import controller.client.ClientController;
import controller.client.GameClient;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import game.Game;
import network.MockSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Timeout(value = 5)
public class ClientTest {

    private static final String USERNAME = "DummyUser";
    private ByteArrayOutputStream out;

    @BeforeEach
    public void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void tearDown() throws IOException {
        out.close();
    }

    private void feedInput (String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    private String getOutput() {
        return out.toString();
    }

    private OutputStream getNetworkOut() {
        return new ByteArrayOutputStream();
    }

    private InputStream getNetworkIn(String input) {
        return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }

    private void assertThatContainsNKeyValuePairs(String message, int numberOfPairsExpected) {
        assertThat(message).matches("[^:]*:[^:]*".repeat(numberOfPairsExpected));
    }

    @Test
    public void testClient1_start_sendJoinRequest() throws IOException {
        ClientController controller = new ClientController();
        GameClient client = new GameClient(controller, USERNAME);

        InputStream networkIn = getNetworkIn("");
        OutputStream networkOut = getNetworkOut();
        MockSocket mockSocket = new MockSocket(networkIn, networkOut);


    }
}
