package Network.SendUtils;

import Network.protocol.MessageFactory;
import Network.protocol.Packet;
import Network.protocol.SnakesProto;

import java.net.InetSocketAddress;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnnouncementSendMaster extends TimerTask {
    private ConcurrentLinkedQueue<Packet> packetsToSend;
    private InetSocketAddress mCastAddrPort;
    private SnakesProto.GameConfig config;
    private SnakesProto.GamePlayers players;

    public AnnouncementSendMaster(ConcurrentLinkedQueue<Packet> packetsToSend, InetSocketAddress mCastAddrPort, SnakesProto.GameConfig config, SnakesProto.GamePlayers players) {
        this.packetsToSend = packetsToSend;
        this.mCastAddrPort = mCastAddrPort;
        this.config = config;
        this.players = players;
    }

    @Override
    public void run() {
        packetsToSend.add(new Packet(MessageFactory.createAnnouncementMsg(config,players),mCastAddrPort));
    }
}
