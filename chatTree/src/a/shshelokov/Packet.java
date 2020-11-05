package a.shshelokov;

import a.shshelokov.Message.Message;

import java.net.InetSocketAddress;

public class Packet {
    public final static int ACCEPT_CHILD_TTL = 1;
    public final static int ACCEPT_CHAT_MESSAGE_TTL = 1;
    public final static int ADOPT_CHILD_TTL = 2;
    public final static int CHAT_MESSAGE_TTL = 4;
    public final static int PING_TTL = 1;
    private int ttl;
    private InetSocketAddress socketAddress;
    private Message message;

    public int getTtl() {
        return ttl;
    }

    public Packet(InetSocketAddress socketAddress, Message message, int ttl) {
        this.socketAddress = socketAddress;
        this.message = message;
        this.ttl = ttl;
    }

    public void decTtl() {
        ttl -= 1;
    }

    public InetSocketAddress getInetSocketAddress() {
        return socketAddress;
    }

    public Message getMessage() {
        return message;
    }

    public void setInetSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}


