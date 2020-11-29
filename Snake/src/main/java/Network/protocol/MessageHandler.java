package Network.protocol;

public class MessageHandler {
    static public void handleAnnouncementMessage(SnakesProto.GameMessage message) {
        System.out.println("I handle Announcement Message ");
    }

    static public void handlePingMessage(SnakesProto.GameMessage message) {
        System.out.println("I handle ping Message ");
    }
}
