package Network;

import Network.RecvUtils.RecvMaster;
import Network.RecvUtils.RecvSlave;
import Network.SendUtils.AnnouncementSendMaster;
import Network.SendUtils.PingSendMaster;
import Network.SendUtils.SendSlave;
import Network.protocol.Packet;
import Network.protocol.SnakesProto;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetNode {
    private final DatagramSocket unicastSocket;
    private final MulticastSocket multicastSocket;
    private final int mCastPort = 9192;
    private final String mCastIp = "239.192.0.4";
    private ConcurrentLinkedQueue<Packet> packetsToSend = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> recvPackets = new ConcurrentLinkedQueue<>();
    private AnnouncementSendMaster announcementSendMaster = null;
    private RecvMaster recvMaster = null;
    private PingSendMaster pingSendMaster = null;
    private SendSlave sendSlave = null;
    private RecvSlave recvSlave = null;
    private RecvSlave multicastRecvSlave = null;
    private final int ANNOUNCEMENT_PERIOD_MS = 1000;
    private final int ANNOUNCEMENT_DELAY_MS = 1000;


    public NetNode(int port) throws IOException {
        unicastSocket = new DatagramSocket(port);
        multicastSocket = new MulticastSocket(mCastPort);
        multicastSocket.joinGroup(new InetSocketAddress(mCastIp, mCastPort), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
    }

    public void initRecvMaster(){
        this.recvMaster = new RecvMaster(recvPackets);
        var recvMaster = new Thread(this.recvMaster, "RecvSlave");
        recvMaster.setDaemon(true);
        recvMaster.start();
    }

    public void initPingSendMaster(int pingDelay, int pingPeriod, ScheduledExecutorService executorService) {
        pingSendMaster = new PingSendMaster(unicastSocket, packetsToSend);
        executorService.scheduleAtFixedRate(pingSendMaster, pingDelay, pingPeriod, TimeUnit.MILLISECONDS);
    }


    public void initAnnouncementSendMaster(SnakesProto.GameConfig config, SnakesProto.GamePlayers players, ScheduledExecutorService executorService) {
        announcementSendMaster = new AnnouncementSendMaster(packetsToSend, new InetSocketAddress(mCastIp, mCastPort), config, players);
        executorService.scheduleAtFixedRate(announcementSendMaster, ANNOUNCEMENT_DELAY_MS, ANNOUNCEMENT_PERIOD_MS, TimeUnit.MILLISECONDS);

    }

    public void initSendSlave() {
        this.sendSlave = new SendSlave(unicastSocket, packetsToSend);
        var sendSlave = new Thread(this.sendSlave, "SendSlave");
        sendSlave.setDaemon(true);
        sendSlave.start();
    }

    public void InitRecvSlave() {
        this.recvSlave = new RecvSlave(unicastSocket, recvPackets);
        var recvSlave = new Thread(this.recvSlave, "RecvSlave");
        recvSlave.setDaemon(true);
        recvSlave.start();
    }

    public void initMulticastRecvSlave() {
        this.multicastRecvSlave = new RecvSlave(multicastSocket, recvPackets);
        var multicastRecvSlave = new Thread(this.multicastRecvSlave, "MulticastRecvSlave");
        multicastRecvSlave.setDaemon(true);
        multicastRecvSlave.start();
    }


    public AnnouncementSendMaster getAnnouncementSendMaster() {
        return announcementSendMaster;
    }

    public void setAnnouncementSendMaster(AnnouncementSendMaster announcementSendMaster) {
        this.announcementSendMaster = announcementSendMaster;
    }

    public SendSlave getSendSlave() {
        return sendSlave;
    }

    public RecvSlave getRecvSlave() {
        return recvSlave;
    }

    public RecvSlave getMulticastRecvSlave() {
        return multicastRecvSlave;
    }
}
