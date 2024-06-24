package communication.messages;

public class AllEndedNotification implements Message {
    String messageType = "AllEndedNotification";

    @Override
    public String getMessageType() {
        return messageType;
    }
}
