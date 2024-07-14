package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/** The type Mock socket. */
public class MockSocket extends Socket {
  private final OutputStream output;
  private InputStream input;
  private boolean isClosed = false;

  /**
   * Instantiates a new Mock socket.
   *
   * @param input the input
   * @param output the output
   */
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

  /**
   * Sets input stream. Method to change the InputStream at runtime.
   *
   * @param newInput the new input
   */
  public void setInputStream(InputStream newInput) {
    this.input = newInput;
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
