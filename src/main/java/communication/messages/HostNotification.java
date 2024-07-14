package communication.messages;

/** The type Host notification. */
public class HostNotification {

  private String messageType = "HostNotification";

  private String host;

  /**
   * Instantiates a new Host notification.
   *
   * @param host the host
   */
  public HostNotification(String host) {
    this.host = host;
  }

  /**
   * Gets host.
   *
   * @return the host
   */
  public String getHost() {
    return host;
  }
}
