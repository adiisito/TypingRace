package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class MockSocket extends Socket{
    private final InputStream input;
    private final OutputStream output;
    private boolean isClosed = false;

    public MockSocket(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void close() throws IOException {
        isClosed = true;
        super.close();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return input;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return output;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}

