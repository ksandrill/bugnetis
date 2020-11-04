package a.shshelokov;

import a.shshelokov.Message.Message;

import java.net.InetSocketAddress;

public class Packet {
    private int tryNumber;
    private InetSocketAddress socketAddress;
    private Message message;

    public Packet(InetSocketAddress socketAddress, Message message) {
        this.socketAddress = socketAddress;
        this.message = message;
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


