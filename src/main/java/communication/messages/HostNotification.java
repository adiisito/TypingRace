package communication.messages;

public class HostNotification {

    public String messageType = "HostNotification";

    public String host;

    public HostNotification(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
