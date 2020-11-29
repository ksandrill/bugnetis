package Network.protocol;

import java.net.InetSocketAddress;

public class Packet {
    SnakesProto.GameMessage message;
    InetSocketAddress addrPort;

    public Packet(SnakesProto.GameMessage message, InetSocketAddress addrPort) {
        this.message = message;
        this.addrPort = addrPort;
    }

    public SnakesProto.GameMessage getMessage() {
        return message;
    }

    public void setMessage(SnakesProto.GameMessage message) {
        this.message = message;
    }

    public InetSocketAddress getAddrPort() {
        return addrPort;
    }

    public void setAddrPort(InetSocketAddress addrPort) {
        this.addrPort = addrPort;
    }
}
