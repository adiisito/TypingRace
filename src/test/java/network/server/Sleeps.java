package network.server;

public class Sleeps {
    public static final int SLEEP_BETWEEN_SOCKET_ACCEPTS = 50; // 50 milliseconds
    public static final int SLEEP_BEFORE_TESTING = 100; // 100 milliseconds

    private Sleeps() {
        // Private constructor to prevent instantiation
    }

    public static void sleepFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
