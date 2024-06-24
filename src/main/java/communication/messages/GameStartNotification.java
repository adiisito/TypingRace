package communication.messages;

public class GameStartNotification extends Message {
    String messageType = "GameStartNotification";
    String text;

    public GameStartNotification(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
