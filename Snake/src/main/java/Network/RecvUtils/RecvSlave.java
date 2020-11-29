package Network.RecvUtils;

import Network.protocol.Packet;
import Network.protocol.SnakesProto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecvSlave implements Runnable {
    private DatagramSocket socket;
    private ConcurrentLinkedQueue<Packet> recvPackets;
    private final int BUFF_SIZE = 10000;

    public RecvSlave(DatagramSocket socket, ConcurrentLinkedQueue<Packet> recvPackets) {
        this.socket = socket;
        this.recvPackets = recvPackets;
    }

    @Override
    public void run() {
        while (true) {
            try {
                recvPacket();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    void recvPacket() throws IOException {
        var data = new byte[BUFF_SIZE];
        DatagramPacket recvPacket = new DatagramPacket(data, data.length);
        socket.receive(recvPacket);
        SnakesProto.GameMessage message = SnakesProto.GameMessage.parseFrom(Arrays.copyOfRange(data, 0, recvPacket.getLength()));
        recvPackets.add(new Packet(message, new InetSocketAddress(recvPacket.getAddress(), recvPacket.getPort())));
        System.out.println(Thread.currentThread().getName() + ": got message from" + recvPacket.getAddress() + ":" + recvPacket.getPort());

    }


}
