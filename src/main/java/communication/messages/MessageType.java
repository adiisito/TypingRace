package communication.messages;

/**
 * Represents the type of a message used in communication between the client and server. This class
 * serves as a base or part of a messaging framework where different types of messages can be
 * identified and processed according to their 'messageType'.
 */
public class MessageType {

  private String messageType;

  /**
   * Instantiates a new Message type.
   *
   * @param messageType the message type
   */
  public MessageType(String messageType) {
    this.messageType = messageType;
  }

  /**
   * Retrieves the type of message this instance represents.
   *
   * @return A string indicating the type of the message.
   */
  public String getMessageType() {
    return messageType;
  }
}
