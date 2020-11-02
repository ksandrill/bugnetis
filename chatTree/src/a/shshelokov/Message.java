package a.shshelokov;

import java.io.Serializable;

public class Message implements Serializable {
    private final MessageType messageType;
    private final String  messageText;
    private final int GUID;

    public Message(MessageType messageType, String messageText, int guid) {
        this.messageType = messageType;
        this.messageText = messageText;
        this.GUID = guid;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getGUID() {
        return GUID;
    }
}
